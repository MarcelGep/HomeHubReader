package homeHubReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReader {

	public final static ObjectMapper objectMapper = new ObjectMapper();

	public JsonReader() {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
//	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
//		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//		System.out.println("######### Categories ##########");
//		readCategoriesFile();
//		
////		System.out.println("######### Custom ##########");
////		readCustomFile();
//
//	}

	public HashMap<String, List<Custom>> readCustomFile(File file) {
		TypeReference<HashMap<String, List<Custom>>> typeReference = new TypeReference<HashMap<String, List<Custom>>>() {};

		try {
			return objectMapper.readValue(file, typeReference);

			//TODO
//			for (List<Custom> entry : customs.values()) {
//				for (Custom c : entry) {
//					System.out.println(c.getName() + "\n" + c.getIcon() + "\n" + c.getDisplay_name() + "\n"
//							+ c.getAppend_divider() + "\n");
//				}
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Category> readCategoriesFile(File file) {
		TypeReference<List<Category>> typeReference = new TypeReference<List<Category>>() {};

		try {
			return objectMapper.readValue(file, typeReference);

			//TODO
//			for (Category c : categories) {
//				System.out.println(c.getName() + "\n" + 
//								   c.getIcon() + "\n" + 
//								   c.getAppend_divider());
//
//				for (SubCategory subCategory : c.getSubcategories()) {
//					System.out.println("\t" + subCategory.getName() + "\n" + "\t" + subCategory.getDisplay_name() + "\n");
//				}
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private static SubCategory createSubCategory(String name, String displayName) {
		SubCategory subCategory = new SubCategory();
		subCategory.setName(name);
		subCategory.setDisplay_name(displayName);

		return subCategory;
	}

	private static Category createCategory(String name, String icon, Boolean appendDivider,
			List<SubCategory> subCategories) {
		Category category = new Category();
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
			
			Category category = createCategory("Untergeschoss", "Icon1.png", true, subCategories);

			objectMapper.writeValue(new File("/home/marcel/Desktop/out.json"), category);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
