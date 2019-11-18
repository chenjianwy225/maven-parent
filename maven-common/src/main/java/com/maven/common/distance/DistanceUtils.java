package com.maven.common.distance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 经纬转换类
 * 
 * @author chenjian
 * @crateDate 2019-11-11
 */
public class DistanceUtils {

	private static Logger logger = LoggerFactory.getLogger(DistanceUtils.class);

	// 地球半径(公里)
	private static final double EARTH_RADIUS = 6378.137;

	private static final double X_PI = 3.14159265358979324 * 3000.0 / 180.0;

	private static final double PI = 3.1415926535897932384626;

	// 偏心率平方
	private static final double EE = 0.00669342162296594323;

	/**
	 * 计算坐标两点之间的距离(公里)
	 * 
	 * @param firstLng
	 *            第一坐标经度
	 * @param firstLat
	 *            第一坐标纬度
	 * @param secondLng
	 *            第二坐标经度
	 * @param secondLat
	 *            第二坐标纬度
	 * @return 两点之间的距离(公里)
	 */
	public static double getDistance(double firstLng, double firstLat,
			double secondLng, double secondLat) {
		double result = 0d;

		try {
			double firstRadLng = rad(firstLng);
			double fisrtRadLat = rad(firstLat);
			double secondRadLng = rad(secondLng);
			double secondRadLat = rad(secondLat);

			double a = firstRadLng - secondRadLng;
			double b = fisrtRadLat - secondRadLat;

			double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(b / 2), 2)
					+ Math.cos(fisrtRadLat) * Math.cos(secondRadLat)
					* Math.pow(Math.sin(a / 2), 2)));
			s = s * EARTH_RADIUS;
			result = Math.round(s * 10000d) / 10000d;

			logger.info("Get distance success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get distance error");
		}

		return result;
	}

	/**
	 * 火星坐标系(GCJ-02)转百度坐标系(BD-09).
	 *
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return 百度坐标系(BD-09)
	 */
	public JSONObject gcj02ToBd09(double lng, double lat) {
		JSONObject jsonObject = new JSONObject();

		try {
			double z = Math.sqrt(lng * lng + lat * lat) + 0.00002
					* Math.sin(lat * X_PI);
			double theta = Math.atan2(lat, lng) + 0.000003
					* Math.cos(lng * X_PI);

			double bd_lng = z * Math.cos(theta) + 0.0065;
			double bd_lat = z * Math.sin(theta) + 0.006;

			jsonObject.put("lng", bd_lng);
			jsonObject.put("lat", bd_lat);

			logger.info("Change coordinate success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change coordinate error");
		}

		return jsonObject;
	}

	/**
	 * 火星坐标系(GCJ02)转国际坐标(WGS84).
	 *
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return 国际坐标(WGS84)
	 */
	public JSONObject gcj02ToWgs84(double lng, double lat) {
		JSONObject jsonObject = new JSONObject();

		try {
			double mg_lng = lng;
			double mg_lat = lat;

			// 判断是否在国内
			if (!outOfChina(lng, lat)) {
				double dlng = transformLng(lng - 105.0, lat - 35.0);
				double dlat = transformLat(lng - 105.0, lat - 35.0);
				double radlat = lat / 180.0 * PI;
				double magic = Math.sin(radlat);
				magic = 1 - EE * magic * magic;
				double sqrtmagic = Math.sqrt(magic);
				dlng = (dlng * 180.0)
						/ (EARTH_RADIUS * 1000 / sqrtmagic * Math.cos(radlat) * PI);
				dlat = (dlat * 180.0)
						/ ((EARTH_RADIUS * 1000 * (1 - EE))
								/ (magic * sqrtmagic) * PI);
				double mglng = lng + dlng;
				double mglat = lat + dlat;

				mg_lng = lng * 2 - mglng;
				mg_lat = lat * 2 - mglat;
			}

			jsonObject.put("lng", mg_lng);
			jsonObject.put("lat", mg_lat);

			logger.info("Change coordinate success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change coordinate error");
		}

		return jsonObject;
	}

	/**
	 * 百度坐标系(BD-09)转火星坐标系(GCJ-02).
	 *
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return 火星坐标系(GCJ-02)
	 */
	public JSONObject bd09ToGcj02(double lng, double lat) {
		JSONObject jsonObject = new JSONObject();

		try {
			double x = lng - 0.0065;
			double y = lat - 0.006;
			double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
			double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);

			double gg_lng = z * Math.cos(theta);
			double gg_lat = z * Math.sin(theta);

			jsonObject.put("lng", gg_lng);
			jsonObject.put("lat", gg_lat);

			logger.info("Change coordinate success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change coordinate error");
		}

		return jsonObject;
	}

	/**
	 * 国际坐标(WGS84)转火星坐标系(GCJ02).
	 *
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return 火星坐标系(GCJ02)
	 */
	public JSONObject wgs84ToGcj02(double lng, double lat) {
		JSONObject jsonObject = new JSONObject();

		try {
			double mg_lng = lng;
			double mg_lat = lat;

			// 判断是否在国内
			if (!outOfChina(lng, lat)) {
				double dlng = transformLng(lng - 105.0, lat - 35.0);
				double dlat = transformLat(lng - 105.0, lat - 35.0);
				double radlat = lat / 180.0 * PI;
				double magic = Math.sin(radlat);
				magic = 1 - EE * magic * magic;
				double sqrtmagic = Math.sqrt(magic);
				dlng = (dlng * 180.0)
						/ (EARTH_RADIUS * 1000 / sqrtmagic * Math.cos(radlat) * PI);
				dlat = (dlat * 180.0)
						/ ((EARTH_RADIUS * 1000 * (1 - EE))
								/ (magic * sqrtmagic) * PI);

				mg_lng = lat + dlat;
				mg_lat = lng + dlng;
			}

			jsonObject.put("lng", mg_lng);
			jsonObject.put("lat", mg_lat);

			logger.info("Change coordinate success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change coordinate error");
		}

		return jsonObject;
	}

	/**
	 * 重新计算数值
	 * 
	 * @param d
	 *            原始数值
	 * @return 新数值
	 */
	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 转换经度
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return 转换后的经度
	 */
	private static double transformLng(double lng, double lat) {
		double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng
				* lat + 0.1 * Math.sqrt(Math.abs(lng));
		ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng
				* PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0
				* PI)) * 2.0 / 3.0;
		return ret;
	}

	/**
	 * 转换纬度
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return 转换后的纬度
	 */
	private static double transformLat(double lng, double lat) {
		double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1
				* lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
		ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng
				* PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI
				/ 30.0)) * 2.0 / 3.0;
		return ret;
	}

	/**
	 * 判断是否在国内(不在国内不做偏移)
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return 是否在国内
	 */
	private static boolean outOfChina(double lng, double lat) {
		return !(lng > 73.66 && lng < 135.05 && lat > 3.86 && lat < 53.55);
	}
}