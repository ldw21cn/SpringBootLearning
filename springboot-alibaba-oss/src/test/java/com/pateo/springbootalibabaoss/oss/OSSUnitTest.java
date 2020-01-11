package com.pateo.springbootalibabaoss.oss;

import com.aliyun.oss.OSSClient;
import com.pateo.springbootalibabaoss.SpringbootAlibabaOssApplication;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = SpringbootAlibabaOssApplication.class)
public class OSSUnitTest {

    //log
    private static final Logger LOG = LoggerFactory.getLogger(OSSUnitTest.class);

    private OSSUnit ossunit = null;
    private OSSClient client = null;
    private String bucketName = "oss-test-my-3389";

    @Before
    public void initUnit(){
        ossunit = new OSSUnit();
        client = OSSUnit.getOSSClient();
    }

    @Test
    public void testCreateBucket() {
        String bucketName = "oss-test-my-3389";
        //创建bucket
        assertEquals(true, OSSUnit.createBucket(client, bucketName));
    }

    @Test
    public void testDeleteBucket(){
        String bucketName = "oss-test-my-3389";
        //删除bucket
        OSSUnit.deleteBucket(client, bucketName);
        //console log :删除oss-test-myBucket成功
    }

    @Test
    public void testUploadObject2OSS(){
        //上传文件
        String flilePathName = "/Users/lvdawei/Downloads/Aerial01.jpg";
        File file = new File(flilePathName);
        String diskName = "datas/image/";
        String md5key = OSSUnit.uploadObject2OSS(client, file, bucketName, diskName);
        LOG.info("上传后的文件MD5数字唯一签名:" + md5key);  //上传后的文件MD5数字唯一签名:A30B046A34EB326C4A3BBD784333B017
    }

    @Test
    public void testGetOSS2InputStream(){
        //获取文件
        try {
            BufferedInputStream bis = new BufferedInputStream(OSSUnit.getOSS2InputStream(client, bucketName, "datas/image/", "Aerial01.jpg"));
            String resfile = "/Users/lvdawei/Downloads/preossimg_res.jpg";
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(resfile)));
            int itemp = 0;
            while((itemp = bis.read()) != -1){
                bos.write(itemp);
            }
            LOG.info("文件获取成功"); //console log :文件获取成功
            bis.close();
            bos.close();
        } catch (Exception e) {
            LOG.error("从OSS获取文件失败:" + e.getMessage(), e);
        }
    }

    @Test
    public void testDeleteFile(){
        //注：文件夹下只有一个文件，则文件夹也会一起删除；如果多个文件，只会删除指定文件名的文件
        //删除文件
        OSSUnit.deleteFile(client, bucketName, "datas/image/", "Aerial01.jpg");
        //console log : 删除oss-test-my下的文件datas/image/preossimg.jpg成功
    }





}