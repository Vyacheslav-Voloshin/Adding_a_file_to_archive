import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Solution  {
    public static void main(String[] args) throws IOException {
        List<Content> contentList = getContent(args[1]);
        FileOutputStream fileOutputStream = new FileOutputStream(args[1]);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

        File mainFile = new File(args[0]);
        zipOutputStream.putNextEntry(new ZipEntry("new/"+mainFile.getName()));
        Files.copy(mainFile.toPath(),zipOutputStream);

        for (Content content:contentList) {
            if (!content.fileName.equals("new/"+mainFile.getName())){
                content.saveZip(zipOutputStream);
            }
        }
        zipOutputStream.close();
    }



    public static List<Content> getContent(String args) throws IOException {
        List<Content> currentList = new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream(args);
        ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
        ZipEntry currentEntry;
        byte[] array = new byte[1024];
        while ((currentEntry  = zipInputStream.getNextEntry())!=null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int lenght;
            while ((lenght = zipInputStream.read(array))>0){
                byteArrayOutputStream.write(array,0,lenght);
            }
            currentList.add(new Content(currentEntry.getName(),byteArrayOutputStream.toByteArray()));
        }
        zipInputStream.close();
        return currentList;
    }

    static class Content{
        String fileName;
        byte[] body;

        public Content(String fileName, byte[] body) {
            this.fileName = fileName;
            this.body = body;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getBody() {
            return body;
        }

        public  void  saveZip (ZipOutputStream zip) throws IOException {
            ZipEntry zipEntry = new ZipEntry(getFileName());
            zip.putNextEntry(zipEntry);
            zip.write(getBody());
            zip.close();
        }
    }

}
