package com.maven.common.xls.map;

/**
 * Style信息
 * 
 * @author chenjian
 * @createDate 2019-10-18
 */
public class StyleEntity {

	// 名称
	private String name;

	// 水平位置(1、常规,2、靠左(缩进),3、居中,4、靠右(缩进),5、填充,6、两端对齐,7、跨列居中,8、分散对齐(缩进))
	private int horizontal = 3;

	// 垂直位置(1、靠上,2、居中,3、靠下,4、两端对齐,5、分散对齐)
	private int vertical = 2;

	// 字体名称
	private String fontName;

	// 字体大小
	private short fontSize;

	// 字体颜色
	private short fontColor;

	// 下划线(1、单下划线,2、双下划线,3、会计单下划线,4、会计双下划线,5、取消下划线)
	private int underline = 5;

	// 上下标(1、上标,2、下标,3、普通)
	private int typeOffset = 3;

	// 背景颜色
	private short backgroundColor;

	// 是否粗体
	private Boolean bold = false;

	// 是否斜体
	private Boolean italic = false;

	// 是否删除线
	private Boolean strikeout = false;

	public StyleEntity(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHorizontal() {
		return horizontal;
	}

	public void setHorizontal(int horizontal) {
		this.horizontal = horizontal;
	}

	public int getVertical() {
		return vertical;
	}

	public void setVertical(int vertical) {
		this.vertical = vertical;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public short getFontSize() {
		return fontSize;
	}

	public void setFontSize(short fontSize) {
		this.fontSize = fontSize;
	}

	public short getFontColor() {
		return fontColor;
	}

	public void setFontColor(short fontColor) {
		this.fontColor = fontColor;
	}

	public int getUnderline() {
		return underline;
	}

	public void setUnderline(int underline) {
		this.underline = underline;
	}

	public int getTypeOffset() {
		return typeOffset;
	}

	public void setTypeOffset(int typeOffset) {
		this.typeOffset = typeOffset;
	}

	public short getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(short backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Boolean getBold() {
		return bold;
	}

	public void setBold(Boolean bold) {
		this.bold = bold;
	}

	public Boolean getItalic() {
		return italic;
	}

	public void setItalic(Boolean italic) {
		this.italic = italic;
	}

	public Boolean getStrikeout() {
		return strikeout;
	}

	public void setStrikeout(Boolean strikeout) {
		this.strikeout = strikeout;
	}
}