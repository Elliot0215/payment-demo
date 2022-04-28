package com.eryue.paymentdemo.config;

import com.wechat.pay.contrib.apache.httpclient.auth.ScheduledUpdateCertificatesVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.security.PrivateKey;

import static org.assertj.core.api.Assertions.assertThat;

@EnableConfigurationProperties
class WxPayConfigTest {

    @Autowired
    private WxPayConfig wxPayConfigUnderTest;

    @BeforeEach
    void setUp() {
        wxPayConfigUnderTest = new WxPayConfig();
    }

    @Test
    void testGetVerifier() {
        String privateKeyPath = wxPayConfigUnderTest.getPrivateKeyPath(); //获取商户私钥
        PrivateKey privateKey = wxPayConfigUnderTest.getPrivateKey(privateKeyPath);
        System.out.println(privateKey);
    }

    @Test
    void testGetWxPayClient() {
        // Setup
        final ScheduledUpdateCertificatesVerifier verifier = new ScheduledUpdateCertificatesVerifier(null,
                "content".getBytes());

        // Run the test
        final CloseableHttpClient result = wxPayConfigUnderTest.getWxPayClient(verifier);

        // Verify the results
    }

    @Test
    void testGetWxPayNoSignClient() {
        // Setup
        // Run the test
        final CloseableHttpClient result = wxPayConfigUnderTest.getWxPayNoSignClient();

        // Verify the results
    }

    @Test
    void testEquals() {
        assertThat(wxPayConfigUnderTest.equals("o")).isTrue();
    }

    @Test
    void testCanEqual() {
        assertThat(wxPayConfigUnderTest.canEqual("other")).isTrue();
    }

    @Test
    void testHashCode() {
        assertThat(wxPayConfigUnderTest.hashCode()).isEqualTo(0);
    }

    @Test
    void testToString() {
        assertThat(wxPayConfigUnderTest.toString()).isEqualTo("result");
    }
}
