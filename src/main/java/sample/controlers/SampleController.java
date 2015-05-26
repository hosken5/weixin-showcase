package sample.controlers;

;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@EnableAutoConfiguration
public class SampleController {
    private Logger log = Logger.getLogger(SampleController.class);
    @Autowired
    protected WxMpConfigStorage wxMpConfigStorage;
    @Autowired
    protected WxMpService wxMpService;
    @Autowired
    protected WxMpMessageRouter wxMpMessageRouter;

    @RequestMapping("/main")
    String main() {
        return "main";
    }


    @RequestMapping("/store")
    @ResponseBody
   public  String maina() {
        return  wxMpConfigStorage.toString() ;
    }



    @RequestMapping(value = { "/oauth2"} )
    public  void  oauth2(HttpServletRequest request, HttpServletResponse  response ) throws IOException {


        log.info("xxxxxxxx"+request.getParameter("code"));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String code = request.getParameter("code");
        try {
            response.getWriter().println("<h1>code</h1>");
            response.getWriter().println(code);

            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);

            response.getWriter().println("<h1>access token</h1>");
            response.getWriter().println(wxMpOAuth2AccessToken.toString());

            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            response.getWriter().println("<h1>user info</h1>");
            response.getWriter().println(wxMpUser.toString());

           //wxMpOAuth2AccessToken = wxMpService.oauth2refreshAccessToken(wxMpOAuth2AccessToken.getRefreshToken());
           //response.getWriter().println("<h1>after refresh</h1>");
           //response.getWriter().println(wxMpOAuth2AccessToken.toString());

        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        response.getWriter().flush();
        response.getWriter().close();
    }

    @RequestMapping(value = { "/", } ,method = RequestMethod.POST)
    public void  mrootG(HttpServletRequest request,HttpServletResponse response) throws IOException {

        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ?
                "raw" :
                request.getParameter("encrypt_type");

        if ("raw".equals(encryptType)) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            if (outMessage != null) {
                response.getWriter().write(outMessage.toXml());
            }
            return;
        }

        if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            String msgSignature = request.getParameter("msg_signature");
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), wxMpConfigStorage, timestamp, nonce, msgSignature);
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            response.getWriter().write(outMessage.toEncryptedXml(wxMpConfigStorage));
            return;
        }

        response.getWriter().println("不可识别的加密类型");
        return;
    }

    @RequestMapping(value = { "/", } ,method = RequestMethod.GET)
    public void  mrootp(HttpServletRequest request,HttpServletResponse response) throws IOException {

        log.info(wxMpService);
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");

        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            response.getWriter().println("非法请求");
            return;
        }

        String echostr = request.getParameter("echostr");
        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            response.getWriter().println(echostr);
            return;
        }
    }
}