package homeHubReader;

import java.util.List;

import javafx.scene.paint.Color;

public class JsonItem {

	String name;
	String icon;
	String append_divider;
	String display_name;
	Color color;

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	List<SubCategory> subcategories;

	public void setSubcategories(List<SubCategory> subcategories) {
		this.subcategories = subcategories;
	}
	
	public List<SubCategory> getSubcategories() {
		return subcategories;
	}

	public String getAppend_divider() {
		return append_divider;
	}
	
	public void setAppend_divider(String append_divider) {
		this.append_divider = append_divider;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
}
