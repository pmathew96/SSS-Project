import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Paul on 4/14/2016.
 */
class Constituency{
    String name;
    int totalelectors;
    List<Candidate> candidates = new ArrayList<Candidate>();
    int generalturnout;
    int postalturnout;
    int totalturnout;
    float totalpercentage;
}
class Candidate{
    String name;
    char sex;
    int age;
    String category;
    String party;
    int generalvotes;
    int postalvotes;
    int totalvotes;
    float votepercentage;
}
public class ECIParser {
    static String commaDelimiter = ",";
    static String newLineSeparator = "\n";
    static String candidateHeader = "Name";
    /*
    Method that takes an object of type constituency and a filewriter as inputs and
    writes the constituency name and the names of all the candidates under the
    constituency into a csv file. This method is called by the readResultsFromFile() method.
     */
    public static void writeResultsIntoFile(Constituency constituency, FileWriter fileWriter){

        try{

            fileWriter.append("Constituency Name:");
            fileWriter.append(commaDelimiter);
            fileWriter.append(constituency.name);
            fileWriter.append(newLineSeparator);
            fileWriter.append(candidateHeader);
            fileWriter.append(newLineSeparator);
            for(Candidate candidate:constituency.candidates){
                fileWriter.append(candidate.name);
                fileWriter.append(newLineSeparator);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    /*
    Method that takes a file as input and reads the details of each constituency and
    all the candidates under that constituency and writes all these details to a
    csv file.
     */
    public static void readResultsFromFile(File resultfile) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("res.csv");
            Constituency constituency = new Constituency();
            Candidate candidate = null;
            int flag = 0, constituencyFlag = 0;
            FileReader fr = new FileReader(resultfile);
            LineNumberReader ln = new LineNumberReader(fr);
            String line;
            String candidateline = "(\\A)(\\d+)(\\s)(\\w*)";
            String constituencyline = "(\\A)(\\d+)([.])(\\s)(\\w+)";
            Pattern candidatepattern = Pattern.compile(candidateline);
            Pattern constituencypattern = Pattern.compile(constituencyline);
            while((line = ln.readLine()) != null){
                candidate = new Candidate();
                if (line.toLowerCase().contains("detailed results")){
                    flag++;
                }
                if (flag >= 2){
                    Matcher candidatematcher = candidatepattern.matcher(line);
                    Matcher constituencymatcher = constituencypattern.matcher(line);
                    if (candidatematcher.find()) {
                        System.out.println(line);
                        candidate.name = line;
                        constituency.candidates.add(candidate);
                        constituencyFlag = 1;
                    }
                    if (constituencymatcher.find()){
                       if(constituencyFlag == 1){       //finding next constituency
                           System.out.println("Next Constituency");
                           writeResultsIntoFile(constituency, fileWriter);
                           constituency = new Constituency();
                        }
                        System.out.println(line);
                        constituency.name = line;
                        constituencyFlag = 0;
                    }
                }
            }
        } catch (IOException e){
            System.out.println(e);
        }finally {
            try{
                fileWriter.flush();
                fileWriter.close();
            }catch(IOException e){
                System.out.println("Error while flushing/closing file");
            }
        }
    }
    public static void main(String[] args) throws IOException{
        File electionresults = new File("C:/Users/Paul/Documents/results.txt");
        readResultsFromFile(electionresults);
    }
}
