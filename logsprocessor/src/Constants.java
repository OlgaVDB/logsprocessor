public final class Constants {
    public static final String START_ANALYSIS = "Starting analysis for task 'SAP MDUS Time Series Item Exporter Electricity'";
    public static final String GET_EXPORT_OCC_START = "#getExportOccurrences() start: ";
    public static final String GET_EXPORT_CAL_START = "EdiSendOnceExportStrategy#getExportCalendars() start";
    public static final String INIT_CALENDAR_START = "(com.energyict.mdw.export.ExportCalendar:initCalendar) - ExportCalendar#initCalendar - start";
    public static final String INIT_CALENDAR_END = "(com.energyict.mdw.export.ExportCalendar:initCalendar) - ExportCalendar#initCalendar -   end";
    public static final String GET_EXPORT_CAL_END = "EdiSendOnceExportStrategy#getExportCalendars()   end";
    public static final String GET_EXPORT_OCC_CAL_START = "DefaultEdiExportStrategy#getExportOccurrences(ExportCalendar) start";
    public static final String GET_VALID_ITEMS_START = "AbstractExportStrategy#getValidItems() start";
    public static final String GET_VALID_ITEMS_END = "AbstractExportStrategy#getValidItems() end";
    public static final String GET_EXPORT_OCC_CAL_END = "DefaultEdiExportStrategy#getExportOccurrences(ExportCalendar) end";
    public static final String GET_EXPORT_OCC_END = "#getExportOccurrences() end: ";
    public static final String FINISHED_ANALYSIS = "Finished analysis for task 'SAP MDUS Time Series Item Exporter Electricity'";
    public static final String FINISHED_DEFAULT_ACTION = "Finished default action";

    public static final String INIT_CALENDAR_ITEM = "(com.energyict.mdw.export.ExportCalendar:initCalendar) - ExportCalendar#initCalendar - name";
    public static final String GET_VALID_ITEM = "AbstractExportStrategy#getValidItems() item";

    public static final String CUS_DATA_GATHERING = "Starting data gathering";
    public static final String CUS_EXPORT_OCC = "SibelgaMessageCollector:logIssues) - Export Occurrence ";
    public static final String CUS_LOG_SENDING_OF = "AbstractTimeSeriesItemExporter:logSendingOf";

    public static final String CUS_COMPLETED_SERVICE_REQUEST = "Completed Service request";

    public static final String SLEEPING = "sleeping for ";
}
