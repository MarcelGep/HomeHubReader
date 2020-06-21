package homeHubReader;

import java.util.List;

public class Category {

	String name;
	String icon;
	String append_divider;
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
}
