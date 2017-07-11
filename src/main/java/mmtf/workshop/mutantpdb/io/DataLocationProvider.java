package mmtf.workshop.mutantpdb.io;

/**
 * Created by Yana Valasatava on 6/28/17.
 */
public class DataLocationProvider {

    private final static String userHome = System.getProperty("user.home");
    private static String uniprotToPdbMappingLocation = getUserHome() + "/spark/parquet/uniprotpdb/20170710/";
    private static String mutationsMappingLocation = getUserHome() + "/spark/parquet/mutationsmapping";

    public static String getUserHome()
    {
        return userHome;
    }

    private String getMutationsFile()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResource("mutations").getPath();
    }

    public static String getUniprotToPdbMappingLocation() {
        return uniprotToPdbMappingLocation;
    }

    public String getMutationsFileLocation() {
        return getMutationsFile();
    }

    public String getOncoKBFileLocation()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResource("oncokb_variants_missense.txt").getPath();
    }

    public static String getMutationsMappingLocation()
    {
        return mutationsMappingLocation;
    }

    public static void setMutationsMappingLocation(String mutationsMappingLocation)
    {
        DataLocationProvider.mutationsMappingLocation = mutationsMappingLocation;
    }

    public static void main(String[] args)
    {
        DataLocationProvider p = new DataLocationProvider();
        System.out.println(p.getOncoKBFileLocation());
    }
}
