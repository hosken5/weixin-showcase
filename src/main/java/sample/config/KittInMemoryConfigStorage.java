package sample.config;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import me.chanjar.weixin.common.util.xml.XStreamInitializer;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;

import java.io.InputStream;


@XStreamAlias("xml")
class KittInMemoryConfigStorage extends WxMpInMemoryConfigStorage {

    @Override
    public String toString() {
        return "SimpleWxConfigProvider [appId=" + appId + ", secret=" + secret + ", accessToken=" + accessToken
                + ", expiresTime=" + expiresTime + ", token=" + token + ", aesKey=" + aesKey + "]";
    }

    public static KittInMemoryConfigStorage fromXml(InputStream is) {
        XStream xstream = XStreamInitializer.getInstance();
        xstream.processAnnotations(KittInMemoryConfigStorage.class);
        return (KittInMemoryConfigStorage) xstream.fromXML(is);
    }

}