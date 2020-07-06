package homeHubReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReader {

	public final static ObjectMapper objectMapper = new ObjectMapper();

	public JsonReader() {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public HashMap<String, List<JsonItem>> readCustomFile(File file) {
		TypeReference<HashMap<String, List<JsonItem>>> typeReference = new TypeReference<HashMap<String, List<JsonItem>>>() {};
		try {
			return objectMapper.readValue(file, typeReference);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<JsonItem> readCategoriesFile(File file) {
		TypeReference<List<JsonItem>> typeReference = new TypeReference<List<JsonItem>>() {};
		try {
			return objectMapper.readValue(file, typeReference);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SubCategory createSubCategory(String name, String displayName) {
		SubCategory subCategory = new SubCategory();
		subCategory.setName(name);
		subCategory.setDisplay_name(displayName);

		return subCategory;
	}

	public static JsonItem createCategory(String name, String icon, Boolean appendDivider,
			List<SubCategory> subCategories) {
		JsonItem category = new JsonItem();
		category.setName(name);
		category.setIcon(icon);
		category.setAppend_divider(String.valueOf(appendDivider));
		category.setSubcategories(subCategories);

		return category;
	}
	
	private static void writeToJson() {		
		try {
			List<SubCategory> subCategories = new ArrayList<SubCategory>();
			subCategories.add(createSubCategory("UG_Licht", "Licht"));
			subCategories.add(createSubCategory("UG_Heizung", "Heizung"));
			subCategories.add(createSubCategory("UG_Test", "Test"));
			
			JsonItem category = createCategory("Untergeschoss", "Icon1.png", true, subCategories);

			objectMapper.writeValue(new File("/home/marcel/Desktop/out.json"), category);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
