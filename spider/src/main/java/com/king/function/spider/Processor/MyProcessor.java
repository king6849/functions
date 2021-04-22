package com.king.function.spider.Processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class MyProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        // 将爬取的网页源代码输出到控制台
        System.out.println(page.getHtml().toString());
    }

    public Site getSite() {
        return Site.me().setSleepTime(100).setRetryTimes(3);
    }

    public static void main(String[] args) {
        Spider.create(new MyProcessor())
                .addUrl("https://blog.csdn.net/")
                .run();
    }

}
