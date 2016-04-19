package com.woniu.base.web.servlet;

import com.google.common.base.Strings;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("serial")
public class VerificationCodeServlet extends HttpServlet {
	public static final String DEFAULT_SESSION_KEY_NAME = "v";

	private static final char[] CHARS = "0123456789".toCharArray();

	private final Font font = new Font("Times New Roman", Font.PLAIN, 17);
	private String sessionKeyName = DEFAULT_SESSION_KEY_NAME;

	private int codeNumber = 4;
	private int width = 70;
	private int height = 18;

	private int offset = 5;
	private int step = 10;

	public void init() throws ServletException {
		String keyName = getInitParameter("keyName");
		if (!Strings.isNullOrEmpty(keyName)) {
			sessionKeyName = keyName;
		}

		String codeNumberStr = getInitParameter("codeNumber");
		if (!Strings.isNullOrEmpty(keyName)) {
			codeNumber = Integer.parseInt(codeNumberStr);
			if (codeNumber < 1) {
				codeNumber = 4;
			}
		}
		width = offset + codeNumber * step + offset;
	}

	private int nextInt(int n) {
		return ThreadLocalRandom.current().nextInt(n);
	}

	private String generateCode(int size) {
		StringBuilder ss = new StringBuilder();
		for (int i = 0; i < size; i++) {
			ss.append(nextInt(CHARS.length));
		}
		return ss.toString();
	}

	private Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + nextInt(bc - fc);
		int g = fc + nextInt(bc - fc);
		int b = fc + nextInt(bc - fc);
		return new Color(r, g, b);
	}

	private void drawDisturbLine(Graphics g) {
		for (int i = 0; i < codeNumber; i++) {
			g.setColor(generateCodeColor());
			int x = offset + nextInt((i + 1) * 15);
			int y = 3 + nextInt(height);
			int xl = offset + nextInt((i + 2) * 15);
			int yl = 3 + nextInt(height - 3);
			g.drawLine(x, y, x + xl, y + yl);
		}
	}

	private Color generateCodeColor() {
		return new Color(20 + nextInt(110), 20 + nextInt(110),
				20 + nextInt(110));
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();
		try {
			g.setColor(getRandColor(200, 250));
			g.fillRect(0, 0, width - 1, height - 1);
			g.setFont(font);

			String code = generateCode(4);
			char chars[] = code.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				char c = chars[i];
				g.setColor(generateCodeColor());
				g.drawString(String.valueOf(c), step * i + offset, 16);
			}
			
			drawDisturbLine(g);

			HttpSession session = request.getSession(true);
			session.setAttribute(sessionKeyName, code);
			ImageIO.write(image, "JPEG", response.getOutputStream());
		} finally {
			g.dispose();
		}
	}

}
