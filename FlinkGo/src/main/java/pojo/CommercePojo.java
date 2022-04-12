package pojo;

import lombok.Data;

//   case class Commerce(InvoiceNo: String, StockCode: String, Description: String, Quantity: String, InvoiceDate: String, UnitPrice: String, CustomerID: String, Country: String)
@Data
public class CommercePojo {
    private String InvoiceNo;
    private String StockCode;
    private String Description;
    private String Quantity;
    private String InvoiceDate;
    private String UnitPrice;
    private String CustomerID;
    private String Country;
}
