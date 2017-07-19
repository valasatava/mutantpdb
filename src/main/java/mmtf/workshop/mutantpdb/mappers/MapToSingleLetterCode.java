package mmtf.workshop.mutantpdb.mappers;

import mmtf.workshop.mutantpdb.io.DataProvider;
import mmtf.workshop.mutantpdb.utils.SparkUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.biojava.nbio.structure.StructureTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yana Valasatava on 7/18/17.
 */
public class MapToSingleLetterCode {

    private static Row addField(Row row, Object field) {

        Object[] fields = new Object[row.length()+1];
        for (int c=0; c < row.length(); c++)
            fields[c] = row.get(c);
        fields[row.length()] = field;

        return RowFactory.create(fields);

    }

    public static Dataset<Row> map(Dataset<Row> df) {

        StructType schemaOrig = df.schema();

        JavaRDD<Row> rdd = df.toJavaRDD().map(new Function<Row, Row>() {
            @Override
            public Row call(Row row) throws Exception {


                String uniprotLetter = null;
                if (row.get(2) != null) {
                    uniprotLetter = String.valueOf(StructureTools.get1LetterCodeAmino(row.getString(2)));
                }
                row = addField(row, uniprotLetter);

                String pdbLetter = null;
                if (row.get(7) != null) {
                    pdbLetter = String.valueOf(StructureTools.get1LetterCodeAmino(row.getString(7)));
                }
                row = addField(row, pdbLetter);

                return row;
            }
        });

        List<StructField> fields = new ArrayList<>();
        StructField field1 = DataTypes.createStructField("uniprotLetter", DataTypes.StringType, true);
        fields.add(field1);
        StructField field2 = DataTypes.createStructField("pdbLetter", DataTypes.StringType, true);
        fields.add(field2);
        StructType schemaAdd = DataTypes.createStructType(fields);

        StructType schema = schemaOrig.merge(schemaAdd);
        Dataset<Row> dataset = SparkUtils.getSparkSession().createDataFrame(rdd, schema)
                .select("uniprotId","uniprotResNum","uniprotLetter","pdbId","chainId","pdbResNum","insCode","pdbLetter","details");
        return dataset;
    }

    public static void main(String[] args) {
        Dataset<Row> df = DataProvider.getUniprotToPdbMismatches();
        Dataset<Row> df2 = map(df);
        df2.show();
    }
}
