package com.abc.web.async;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 监听当sping启动事件，启动轮询线程，轮询订单队列
 */
@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent>{

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MockQueue mockQueue;
    @Autowired
    private DeferredResultHolder deferredResultHolder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //模拟在单独线程里轮询消息队列，处理完成的订单
        new Thread(()->{
            while (true){
                //如果订单完成
                if (StringUtils.isNotBlank(mockQueue.getCompleteOrder())){
                    String orderNubmer = mockQueue.getCompleteOrder();
                    logger.info("返回订单处理结果："+orderNubmer);
                    //调用setResult会触发响应结果给前台。
                    deferredResultHolder.getMap().get(orderNubmer).setResult("place order success");
                    //清空已处理的订单
                    mockQueue.setCompleteOrder(null);

                }else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
