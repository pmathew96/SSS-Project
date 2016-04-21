import java.io.*;
import java.util.*;
/**
 * Created by Paul on 4/14/2016.
 */
class Consituency{
    String constituencyname;
    int totalelectors;
    List<Candidate> constituencycandidate = new ArrayList<Candidate>();
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