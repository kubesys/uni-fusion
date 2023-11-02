package com.qnkj.core.plugins.file.util;

import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import io.minio.*;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MinIOUtils {
    static MinioClient minioClient = null;
    private static void init() {
        if (minioClient == null) {
            minioClient = MinioClient.builder()
                    .endpoint(MinIOConfig.url)
                    .credentials(MinIOConfig.accessKey, MinIOConfig.secretKey)
                    .build();
        }
    }
    public static String uploadMinio(byte[] bytes, String filePath) throws Exception {
            init();
            InputStream input = null;
            try {
                input = new ByteArrayInputStream(bytes);
                if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(MinIOConfig.bucket).build())) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(MinIOConfig.bucket).build());
                }
                Date date = new Date();
                SimpleDateFormat year = new SimpleDateFormat("yyyy");
                SimpleDateFormat month = new SimpleDateFormat("MMM", Locale.ENGLISH);
                String objectPath = "";
                if (!"0".equals(SupplierUtils.getSupplierid())) {
                    objectPath = "/" + SupplierUtils.getSupplierid();
                }
                objectPath += "/" + year.format(date) + "/" + month.format(date);
                objectPath += "/" + filePath;
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(MinIOConfig.bucket)
                                .stream(input, input.available(), -1)
                                .object(objectPath)
                                .build()
                );
                return "/" + MinIOConfig.bucket + objectPath;
            } catch (Exception e) {
                throw e;
            } finally {
                if (Objects.nonNull(input)) {
                    input.close();
                }
            }
    }

    public static byte[] downloadMinio(String bucket, String url) throws Exception {
        init();
        GetObjectArgs objectArgs = GetObjectArgs.builder()
                .bucket(bucket)
                .object(url)
                .build();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = response.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
            }
            return out.toByteArray();
        }catch (Exception e) {
            throw e;
        } finally {
            out.close();
        }
    }


}
