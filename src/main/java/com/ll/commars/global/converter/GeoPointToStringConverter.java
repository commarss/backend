// package com.ll.commars.global.converter;
//
// import org.springframework.core.convert.converter.Converter;
// import org.springframework.data.convert.WritingConverter;
// import org.springframework.data.elasticsearch.core.geo.GeoPoint;
// import org.springframework.stereotype.Component;
//
// @Component
// @WritingConverter
// public class GeoPointToStringConverter implements Converter<GeoPoint, String> {
//
// 	@Override
// 	public String convert(GeoPoint source) {
// 		if (source == null) {
// 			return null;
// 		}
// 		// GeoPoint 객체를 "위도,경도" 으로 변환
// 		return source.getLat() + "," + source.getLon();
// 	}
// }
