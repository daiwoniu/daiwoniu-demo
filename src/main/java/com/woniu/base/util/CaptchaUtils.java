package com.woniu.base.util;

import org.patchca.background.BackgroundFactory;
import org.patchca.color.ColorFactory;
import org.patchca.color.RandomColorFactory;
import org.patchca.filter.ConfigurableFilterFactory;
import org.patchca.filter.library.AbstractImageOp;
import org.patchca.filter.library.WobbleImageOp;
import org.patchca.font.RandomFontFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.text.renderer.BestFitTextRenderer;
import org.patchca.text.renderer.TextRenderer;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by liguoxiang on 2016-5-9.
 */
public class CaptchaUtils {
    private CaptchaUtils(){}

    public static final String PNG_Captcha = "png";
    public static final String BMP_Captcha = "bmp";
    public static final String GIF_Captcha = "gif";
    public static final String JPG_Captcha = "jpg";
    public static final String JPEG_Captcha = "jpeg";
    public static final String WBMP_Captcha = "wbmp";

    private static ConfigurableCaptchaService simpleCaptchaService = new ConfigurableCaptchaService();
    private static boolean isInitSimpleCaptchaService = false;

    /**
     * 生成验证码并写入输出流
     * @param imgType 图片类型
     *                <br/>&nbsp;&nbsp;&nbsp;使用{@link #PNG_Captcha},{@link #BMP_Captcha},{@link #GIF_Captcha} ,{@link #JPG_Captcha},{@link #JPEG_Captcha} ,{@link #WBMP_Captcha}替代.
     * @param outputStream 输出流
     * @return 返回生成的验证码字符串
     * @throws IOException
     */
    public static String getSimpleCaptchaAndWriteImage(String imgType, OutputStream outputStream) throws IOException {
        initSimpleCaptchaService();
        String token = EncoderHelper.getChallangeAndWriteImage(simpleCaptchaService, imgType, outputStream);
        return token;
    }

    private static void initSimpleCaptchaService(){
        if(!isInitSimpleCaptchaService){
            synchronized (simpleCaptchaService){
                if(!isInitSimpleCaptchaService){
                    // 颜色创建工厂,使用一定范围内的随机色
                    ColorFactory colorFactory = new RandomColorFactory();
                    simpleCaptchaService.setColorFactory(colorFactory);

                    // 字体创建工厂,使用一定范围内的随机大小
                    RandomFontFactory fontFactory = new RandomFontFactory();
                    fontFactory.setMaxSize(32);
                    fontFactory.setMinSize(28);
                    simpleCaptchaService.setFontFactory(fontFactory);

                    // 随机字符生成器,去除掉容易混淆的字母和数字,如o和0等
                    RandomWordFactory wordFactory = new RandomWordFactory();
                    wordFactory.setCharacters("abcdefghkmnpqstwxyz23456789");
                    wordFactory.setMaxLength(5);
                    wordFactory.setMinLength(4);
                    simpleCaptchaService.setWordFactory(wordFactory);

                    // 自定义验证码图片背景
                    MyCustomBackgroundFactory backgroundFactory = new MyCustomBackgroundFactory();
                    simpleCaptchaService.setBackgroundFactory(backgroundFactory);

                    // 图片滤镜设置
                    ConfigurableFilterFactory filterFactory = new ConfigurableFilterFactory();
                    List<BufferedImageOp> filters = new ArrayList<BufferedImageOp>();
                    WobbleImageOp wobbleImageOp = new WobbleImageOp();
                    wobbleImageOp.setEdgeMode(AbstractImageOp.EDGE_MIRROR);
                    wobbleImageOp.setxAmplitude(2.0);
                    wobbleImageOp.setyAmplitude(1.0);
                    filters.add(wobbleImageOp);
                    filterFactory.setFilters(filters);
                    simpleCaptchaService.setFilterFactory(filterFactory);

                    // 文字渲染器设置
                    TextRenderer textRenderer = new BestFitTextRenderer();
                    textRenderer.setBottomMargin(3);
                    textRenderer.setTopMargin(3);
                    simpleCaptchaService.setTextRenderer(textRenderer);

                    // 验证码图片的大小
                    simpleCaptchaService.setWidth(82);
                    simpleCaptchaService.setHeight(32);

                    isInitSimpleCaptchaService = true;
                }
            }
        }
    }
    /**
     * 自定义验证码图片背景,主要画一些噪点和干扰线
     */
    private static class MyCustomBackgroundFactory implements BackgroundFactory {
        public void fillBackground(BufferedImage image) {
            Random random = new Random();
            Graphics graphics = image.getGraphics();
            // 验证码图片的宽高
            int imgWidth = image.getWidth();
            int imgHeight = image.getHeight();
            // 填充为白色背景
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, imgWidth, imgHeight);
            // 画100个噪点(颜色及位置随机)
            for(int i = 0; i < 100; i++) {
                // 随机颜色
                int rInt = random.nextInt(255);
                int gInt = random.nextInt(255);
                int bInt = random.nextInt(255);
                graphics.setColor(new Color(rInt, gInt, bInt));
                // 随机位置
                int xInt = random.nextInt(imgWidth - 3);
                int yInt = random.nextInt(imgHeight - 2);
                // 随机旋转角度
                int sAngleInt = random.nextInt(360);
                int eAngleInt = random.nextInt(360);
                // 随机大小
                int wInt = random.nextInt(6);
                int hInt = random.nextInt(6);
                graphics.fillArc(xInt, yInt, wInt, hInt, sAngleInt, eAngleInt);
                // 画5条干扰线
                if (i % 20 == 0) {
                    int xInt2 = random.nextInt(imgWidth);
                    int yInt2 = random.nextInt(imgHeight);
                    graphics.drawLine(xInt, yInt, xInt2, yInt2);
                }
            }
        }
    }

}
