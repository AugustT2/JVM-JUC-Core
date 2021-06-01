package javase.iodemo.jsontoexcel;

/**
 * @author EDing3
 * @create 2021/6/1 12:48
 * 将项目放入Springboot项目中执行，JSON文件转excel
 */
public class JsonToString {
    /*public static void main(String[] args) throws IOException {
        File inputFile = new File("C:\\logs\\aa");
        File[] files = inputFile.listFiles();
        //jackson的jar包
        ObjectMapper objectMapper = new ObjectMapper();
        List<PoushengOrderConfirmation> allData = new ArrayList<>();
        for (File file : files) {
            String jsonStr = ConvertUtil.convertTextBodyToBean(new InputStreamReader(new FileInputStream(file)));
            List<PoushengOrderConfirmation> temp = objectMapper.readValue(jsonStr,new TypeReference<List<PoushengOrderConfirmation>>() { });
            allData.addAll(temp);
        }
        String fileName = "C:/logs/bb/cc.xlsx";
        //导easyexcel的jar包
        EasyExcel.write(fileName, PoushengOrderConfirmation.class).sheet("模板").doWrite(allData);
    }*/
}
