package mmtf.workshop.mutantpdb.filters;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/** This Interface provides custom filtering logic based on the dataframe.
 *
 * User can create the custom filter class by implementing this filter and invoke the filter based methods
 * to apply the filter.
 *
 * Created by Yana Valasatava on 7/14/17.
 *
 * @author Yana Valasatava
 *
 */
public interface IDataframeFilter
{
    Dataset<Row> filter(Dataset<Row> data);
}