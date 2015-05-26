package sample.config;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

/**
 * Created by hongpf on 15/4/22.
 */
@Configuration
public class weixinConfig {

   @Bean
   public WxMpConfigStorage getwxMpConfigStorage(){
       InputStream is1 = ClassLoader.getSystemResourceAsStream("weixin-config.xml");
       KittInMemoryConfigStorage config = KittInMemoryConfigStorage.fromXml(is1);
       return  config ;
   }
    @Bean
    public  WxMpService wxMpService (WxMpConfigStorage wxMpConfigStorage){
        WxMpService  service =  new  WxMpServiceImpl();
        service.setWxMpConfigStorage(wxMpConfigStorage);
        return service  ;
    }

    @Bean
    public  WxMpMessageRouter wxMpMessageRouter( WxMpService  wxMpService ){

        WxMpMessageHandler logHandler = new DemoLogHandler();
        WxMpMessageHandler textHandler = new DemoTextHandler();
        WxMpMessageHandler imageHandler = new DemoImageHandler();
        WxMpMessageHandler oauth2handler = new DemoOAuth2Handler();
        DemoGuessNumberHandler guessNumberHandler = new DemoGuessNumberHandler();


        WxMpMessageRouter wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
        wxMpMessageRouter
            .rule().handler(logHandler).next()
            .rule().msgType(WxConsts.XML_MSG_TEXT).matcher(guessNumberHandler).handler(guessNumberHandler).end()
            .rule().async(false).content("哈哈").handler(textHandler).end()
            .rule().async(false).content("图片").handler(imageHandler).end()
            .rule().async(false).content("o").handler(oauth2handler).end();
        return  wxMpMessageRouter;
    }
}
