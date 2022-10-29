import Model.OutputRecord;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * <h3>client-part2</h3>
 *
 * @author Zhe Xi
 * @description <p></p>
 * @date 2022-10-07 15:32
 **/
public class CSVWriter {
    public static void outputCSVFile(List<OutputRecord> records, String filepath, String name)
        throws IOException {
        File csv = new File(filepath + name);
        if(csv.exists()) {
            csv.delete();
        }
        FileWriter writer = new FileWriter(csv);
        writer.write("StartTime,RequestType,Latency,ResponseCode\n");
        for (OutputRecord record: records) {
            writer.write(record.toString() + "\n");
        }
        writer.close();
    }
}
