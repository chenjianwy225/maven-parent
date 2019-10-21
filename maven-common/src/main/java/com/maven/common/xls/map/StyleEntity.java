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

	// 水平位置
	private int horizontal = 2;

	// 垂直位置
	private int vertical = 2;

	// 字体大小
	private short fontSize;

	// 字体颜色
	private short fontColor;

	// 是否粗体
	private Boolean bold = false;

	// 是否斜体
	private Boolean italic = false;

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
}