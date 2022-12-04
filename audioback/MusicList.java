package audioback;

import java.io.File;

public class MusicList {
    private File folder;
    private String [] musicName;
    private int currentIndex;
    private int length;

    public MusicList(String folderPath){
        folder = new File(folderPath);
        currentIndex = 0;
        if(folder.exists()){
            File[] listOfFiles = folder.listFiles();
            musicName = new String[listOfFiles.length];
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String type = (listOfFiles[i].getName()).substring((listOfFiles[i].getName()).length()-3, (listOfFiles[i].getName()).length());
                    if(type.equals("mp3") || type.equals("wav"))
                        musicName[currentIndex++] = listOfFiles[i].getName();
                }
            }
        }
        length = currentIndex;
        currentIndex = 0;
    }

    public String getMusicPath(){
        return (folder.toURI().toString() + musicName[currentIndex++]).substring(6);
    }

    public String getMusicPath(int index){
        currentIndex = index;
        return getMusicPath();
    }

    public String toString(){
        String result = "";
        for(int i = 0; i < length; i++){
            result += musicName[i]+"\n";
        }
        StringBuffer resulBuffer = new StringBuffer(result);
        resulBuffer.setLength(resulBuffer.length()-1);
        return new String(resulBuffer);
    }

    // Hướng dẫn sử dụng ae
    public static void main(String[] args) {
        MusicList musicList = new MusicList("D:/GitHub/Audio-Player/audioback");
        System.out.println(musicList.toString());
        System.out.println(musicList.getMusicPath());
        System.out.println(musicList.getMusicPath());
        System.out.println(musicList.getMusicPath());
    }
}
