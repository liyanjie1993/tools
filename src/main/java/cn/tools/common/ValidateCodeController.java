package cn.tools.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright Unicom(HN) 2015. All rights reserved.
 *
 * @File: ValidateCodeController.java
 * @Package: com.unicom.core.controller
 * @ClassName: ValidateCodeController
 * @Description: 生成随机验证码
 * @Author Eleazar zhangsq
 * @Date 2015年11月3日
 * @Version V1.0
 * 
 */

@RestController
public class ValidateCodeController  {
	@GetMapping(path = "/validatecode",  produces = "text/plain; charset=utf-8")
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("image/jpeg");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expiresponse", 0);
		HttpSession session = request.getSession();
		// 在内存中创建图象
		int width = 60, height = 20;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = image.getGraphics();
		// 生成随机类
		Random random = new Random();
		// 设定背景色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		// 设定字体
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		// 画边框
		g.drawRect(0, 0, width - 1, height - 1);
		// 使图象中的认证码不易被其它程序探测到
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 60; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		// 取随机产生的认证码(4位数字)
		String sRand = "";
		for (int i = 0; i < 4; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			// 将认证码显示到图象中
			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			g.drawString(rand, 13 * i + 6, 16);
		}
		// 将认证码存入SESSION
		session.setAttribute("verifycode", sRand);
		// 图象生效
		g.dispose();
		// 输出图象到页面
		try {
			ImageIO.write(image, "JPEG", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			Logger.getLogger(getClass()).error("ValidateCodeController抛出异常", e);
		}
	}

	/**
	 * @Title: getRandColor
	 * @Description: 获取随机颜色
	 * @param i
	 * @param j
	 * @return
	 * @return Color
	 * @throws
	 *
	 */
	private Color getRandColor(int i, int j) {
		Random random = new Random();
		if (i > 255) {
			i = 255;
		}
		if (j > 255) {
			j = 255;
		}
		int k = i + random.nextInt(j - i);
		int l = i + random.nextInt(j - i);
		int i1 = i + random.nextInt(j - i);
		return new Color(k, l, i1);
	}

}
