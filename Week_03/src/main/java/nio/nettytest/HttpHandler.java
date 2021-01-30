package nio.nettytest;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.ReferenceCountUtil;
import nio.Constant;
import nio.MyOKHttlpClient;
import nio.filter.HeaderHttpRequestFilter;
import nio.filter.HeaderHttpResponseFilter;
import nio.filter.HttpRequestFilter;
import nio.filter.HttpResponseFilter;
import nio.router.HttpEndpointRouter;
import nio.router.RandomHttpEndpointRouter;

import java.util.Arrays;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpHandler extends ChannelInboundHandlerAdapter {
    private HttpRequestFilter reqentFilter = new HeaderHttpRequestFilter();
    private HttpResponseFilter responseFilter = new HeaderHttpResponseFilter();
    private HttpEndpointRouter router = new RandomHttpEndpointRouter();
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            //logger.info("channelRead流量接口请求开始，时间为{}", startTime);
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            String uri = fullRequest.uri();
            handlerTest(fullRequest, ctx, reqentFilter);
    
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void handlerTest(FullHttpRequest fullRequest, ChannelHandlerContext ctx, HttpRequestFilter filter) {
        //请求过滤器
        filter.filter(fullRequest, ctx);

        String fullRequestAddHeader = fullRequest.headers().get("yp01");
        System.out.println(fullRequestAddHeader);

        FullHttpResponse response = null;

        //随机路由功能
        String requestUrl = router.route(Arrays.asList(Constant.urlsString.split(",")));

        try {
            String value = MyOKHttlpClient.getClientResponse(requestUrl) + "   fullRequestAddHeader==> " + fullRequestAddHeader;

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());

            //响应过滤器
            responseFilter.filter(response);

        } catch (Exception e) {
            System.out.println("处理出错:"+e.getMessage());
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
