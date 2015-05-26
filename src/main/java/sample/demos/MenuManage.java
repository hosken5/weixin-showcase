package sample.demos;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxMenu;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutImageMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hongpf on 15/4/23.
 */
public class MenuManage {

    static WxMpInMemoryConfigStorage config ;
    static String  toUser = "omJl7t7o3d9FFlrrgUU_rHeu5crg" ;

    public static void main(String []  args ) throws WxErrorException, IOException {
        config = new WxMpInMemoryConfigStorage();
        config.setAppId("Wx6abf1fd4bc589d0a"); // 设置微信公众号的appid
        config.setSecret("69713d835931b4597df89b3a838a5601"); // 设置微信公众号的app corpSecret
        config.setToken("111"); // 设置微信公众号的token
        config.setAesKey("bKdT43K1gmBuKxynasE0ep1TOH3Uyn7ZufZmMAuXxlJ"); // 设置微信公众号的EncodingAESKey

        WxMpServiceImpl wxService = new WxMpServiceImpl();
        wxService.setWxMpConfigStorage(config);

     // 用户的openid在下面地址获得
     // https://mp.weixin.qq.com/debug/cgi-bin/apiinfo?t=index&type=用户管理&form=获取关注者列表接口%20/user/get
     //   String openid = "omJl7t7o3d9FFlrrgUU_rHeu5crg";
     //   WxMpCustomMessage message = WxMpCustomMessage.TEXT().toUser(openid).content("Hello World").build();
     //   wxService.customMessageSend(message);
        MenuManage m =   new MenuManage()  ;
        //m.menuget(wxService) ;
        //m.menuCreate(wxService);
        //m.erweima(wxService);
        //m.shortUrl(wxService);
        //m.voice(wxService);
        //m.mp4(wxService);
        //m.picture(wxService);
        m.userinfo(wxService);
    }


    public void userinfo(WxMpServiceImpl  wxMpService) throws WxErrorException {
        WxMpUser u =   wxMpService.userInfo(toUser, null) ;
        System.out.println(u);
    }


    public void OAuth2(WxMpServiceImpl  wxMpService){
        wxMpService.oauth2buildAuthorizationUrl(WxConsts.OAUTH2_SCOPE_USER_INFO, null) ;
    }

    public void picture (WxMpServiceImpl  wxMpService ) throws WxErrorException, IOException {

        WxMediaUploadResult wxMediaUploadResult = wxMpService
                .mediaUpload(WxConsts.MEDIA_IMAGE, WxConsts.FILE_JPG,
                        ClassLoader.getSystemResourceAsStream("mm.jpeg"));

        WxMpCustomMessage m =
                WxMpCustomMessage.IMAGE()
                        .mediaId(wxMediaUploadResult.getMediaId())
                        .toUser(toUser)
                        .build();

        wxMpService.customMessageSend(m);
    }

    public void mp4 (WxMpServiceImpl  wxMpService ) throws WxErrorException, IOException {

        WxMediaUploadResult wxMediaUploadResult = wxMpService
                .mediaUpload(WxConsts.MEDIA_VIDEO, WxConsts.FILE_MP4,
                        ClassLoader.getSystemResourceAsStream("mm.mp4"));

        WxMpCustomMessage m =
                WxMpCustomMessage.VIDEO()
                        .mediaId(wxMediaUploadResult.getMediaId())
                        .toUser("omJl7t7o3d9FFlrrgUU_rHeu5crg")
                        .build();

        wxMpService.customMessageSend(m);
    }



    public void voice (WxMpServiceImpl  wxMpService ) throws WxErrorException, IOException {

        WxMediaUploadResult wxMediaUploadResult = wxMpService
                .mediaUpload(WxConsts.MEDIA_VOICE, WxConsts.FILE_MP3,
                        ClassLoader.getSystemResourceAsStream("mm.mp3"));

        WxMpCustomMessage m =
            WxMpCustomMessage.VOICE()
                .mediaId(wxMediaUploadResult.getMediaId())
                    .toUser("omJl7t7o3d9FFlrrgUU_rHeu5crg")
                .build();
        wxMpService.customMessageSend(m);
    }




    public void shortUrl (WxMpServiceImpl  wxMpService ) throws WxErrorException {
        String stringshortUrl = wxMpService.shortUrl("www.qq.com");
        WxMpCustomMessage m =
        WxMpCustomMessage
                .TEXT()
                .toUser("omJl7t7o3d9FFlrrgUU_rHeu5crg")
                .content("www.baidu.com")
                .build();
        wxMpService.customMessageSend(m);

    }



    public void erweima(WxMpServiceImpl  wxMpService ) throws WxErrorException {
        WxMpQrCodeTicket ticket =   wxMpService.qrCodeCreateLastTicket(1) ;
        File file = wxMpService.qrCodePicture(ticket);
        //   InputStream inputStream = ...;
        //  File file = ...;
        //WxMediaUploadResult res = wxMpService.mediaUpload(mediaType, fileType, inputStream);
        // 或者
        WxMediaUploadResult res = wxMpService.mediaUpload(WxConsts.MEDIA_IMAGE, file);
        res.getType();
        res.getCreatedAt();
        res.getMediaId();
        res.getThumbMediaId();


        WxMpCustomMessage m
                = WxMpCustomMessage
                .IMAGE()
                .mediaId(res.getMediaId())
                .toUser("omJl7t7o3d9FFlrrgUU_rHeu5crg")
                .build();
        wxMpService.customMessageSend(m);

    }

    public void menuCreate ( WxMpServiceImpl  wxMpService ) throws WxErrorException {
        List<WxMenu.WxMenuButton > butts  =  new ArrayList<WxMenu.WxMenuButton>() ;
        WxMenu wxMenu = new WxMenu();

        WxMenu.WxMenuButton  one =  new WxMenu.WxMenuButton();
        one.setType("click");
        one.setName("one");
        one.setKey("one");
        WxMenu.WxMenuButton  two =  new WxMenu.WxMenuButton();
        two.setType("click");
        two.setName("two");
        two.setKey("two");
        WxMenu.WxMenuButton  three =  new WxMenu.WxMenuButton();
        three.setType("click");
        three.setName("three");
        three.setKey("three");

        WxMenu.WxMenuButton  bt1 =  new WxMenu.WxMenuButton();
        bt1.setName("url180");
        bt1.setKey("key1");
        bt1.setType(WxConsts.BUTTON_VIEW);
        bt1.setUrl("http://www.yimei180.com/");

        List<WxMenu.WxMenuButton > butts2  =  new ArrayList<WxMenu.WxMenuButton>() ;
        butts2.add(bt1) ;
        one.setSubButtons(butts2);


        butts.add(one);
        butts.add(two);
        butts.add(three);

        wxMenu.setButtons(butts);

        wxMpService.menuCreate(wxMenu);
        System.out.println("创建成功");

    }

    public void menuget ( WxMpServiceImpl  wxMpService ) throws WxErrorException {
        WxMenu wxMenu = wxMpService.menuGet() ;
        System.out.println(wxMenu.toString());
        //WxMenu{buttons=[WxMenuButton{type='click', name='今日歌曲', key='V1001_TODAY_MUSIC', url='null', subButtons=[]}, WxMenuButton{type='null', name='菜单', key='null', url='null', subButtons=[WxMenuButton{type='view', name='搜索', key='null', url='http://www.soso.com/', subButtons=[]}, WxMenuButton{type='view', name='视频', key='null', url='http://v.qq.com/', subButtons=[]}, WxMenuButton{type='click', name='赞一下我们', key='V1001_GOOD', url='null', subButtons=[]}]}]}
    }
}
