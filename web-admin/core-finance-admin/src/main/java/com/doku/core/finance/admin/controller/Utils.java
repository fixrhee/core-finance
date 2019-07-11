package com.doku.core.finance.admin.controller;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public abstract class Utils {

	// public static String formatAmount(BigDecimal amount) {
	// DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
	// symbols.setGroupingSeparator('.');
	// symbols.setDecimalSeparator(',');
	// DecimalFormat df = new DecimalFormat("#,##0.00", symbols);
	// return "Rp." + df.format(amount);
	// }

	public static String GenerateRandomNumber() {
		String ts = String.valueOf(System.currentTimeMillis());
		String rand = UUID.randomUUID().toString();
		return DigestUtils.sha1Hex(ts + rand);
	}

	public static String GenerateRandomNumber(int charLength) {
		return String.valueOf(charLength < 1 ? 0
				: new Random().nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
						+ (int) Math.pow(10, charLength - 1));
	}

	public static String getRandomNumberInRange(String max) {
		Random r = new Random();
		Long lrand = r.longs(1, (Long.valueOf(max) + 1)).limit(1).findFirst().getAsLong();
		String rand = String.valueOf(lrand);
		int size = max.length();
		String pad;
		if (rand.length() == 1) {
			pad = StringUtils.rightPad(rand, size, '0');
		} else {
			pad = StringUtils.leftPad(rand, size, '0');
		}
		return pad;
	}

	public static String GenerateTransactionNumber() {
		int randomNum = ThreadLocalRandom.current().nextInt(100000, 999999 + 1);
		return String.valueOf(randomNum);
	}

	public static String getMD5Hash(String source) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			String hex = (new HexBinaryAdapter()).marshal(md5.digest(source.getBytes("UTF-8")));
			return hex.toLowerCase();
		} catch (Exception ex) {
			return null;
		}
	}

	public static String GetCurrentDate() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(date);
	}

	public static String GetDate(String form) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat(form);
		return format.format(date);
	}

	public static String GetFutureDate(String form, Integer add) {
		SimpleDateFormat format = new SimpleDateFormat(form);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, add);
		Date newDate = c.getTime();
		return format.format(newDate);
	}

	public static String generateTraceNum() {
		String ts = String.valueOf(System.currentTimeMillis());
		String rand = UUID.randomUUID().toString().replace("-", "");
		Random rnd = new Random();
		int n = 10000000 + rnd.nextInt(90000000);
		String a = Integer.toString(n);
		return ts + a + rand;
	}

	public static String formatDate(Date date) {
		return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:SS");
	}

	public static String formatSimpleDate(Date date) {
		return DateFormatUtils.format(date, "MM/dd/yyyy");
	}

	public static final Map<String, Long> times = new LinkedHashMap<>();

	static {
		times.put("year", TimeUnit.DAYS.toMillis(365));
		times.put("month", TimeUnit.DAYS.toMillis(30));
		times.put("week", TimeUnit.DAYS.toMillis(7));
		times.put("day", TimeUnit.DAYS.toMillis(1));
		times.put("hour", TimeUnit.HOURS.toMillis(1));
		times.put("minute", TimeUnit.MINUTES.toMillis(1));
		times.put("second", TimeUnit.SECONDS.toMillis(1));
	}

	public static String toRelative(long duration, int maxLevel) {
		StringBuilder res = new StringBuilder();
		int level = 0;
		for (Map.Entry<String, Long> time : times.entrySet()) {
			long timeDelta = duration / time.getValue();
			if (timeDelta > 0) {
				res.append(timeDelta).append(" ").append(time.getKey()).append(timeDelta > 1 ? "s" : "").append(", ");
				duration -= time.getValue() * timeDelta;
				level++;
			}
			if (level == maxLevel) {
				break;
			}
		}
		if ("".equals(res.toString())) {
			return "0 seconds ago";
		} else {
			res.setLength(res.length() - 2);
			res.append(" ago");
			return res.toString();
		}
	}

	public static String toRelative(long duration) {
		return toRelative(duration, times.size());
	}

	public static String toRelative(Date start, Date end) {
		assert start.after(end);
		return toRelative(end.getTime() - start.getTime());
	}

	public static String toRelative(Date start, Date end, int level) {
		assert start.after(end);
		return toRelative(end.getTime() - start.getTime(), level);
	}

	public static String toJSON(Object obj) {
		try {
			ObjectMapper jsonMapper = new ObjectMapper();
			jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			String json = jsonMapper.writeValueAsString(obj);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static XMLGregorianCalendar stringDateToXML(String date) throws Exception {
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		Date fromatedDate = format.parse(date);
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(fromatedDate);
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
	}

	public static XMLGregorianCalendar dateToXML(Date date) throws DatatypeConfigurationException {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(date);
		XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		return date2;
	}
}
