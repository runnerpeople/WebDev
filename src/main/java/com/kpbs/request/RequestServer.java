package com.kpbs.request;

import com.kpbs.model.DBData;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestServer {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    private DBData data;
    private String id;
    private String webix_operation;

    private boolean isValid = true;

    public RequestServer() {
    }

    public RequestServer(DBData data, String id, String webix_operation,boolean isValid) {
        this.data = data;
        this.id = id;
        this.webix_operation = webix_operation;
        this.isValid = isValid;
    }

    public RequestServer(MultiValueMap<String,Object> request) {
        this.data = new DBData();
        request.forEach((key, value) -> {
            Field field;
            boolean flagContinue = true;
            try {
                field = this.data.getClass().getDeclaredField(key);
                field.setAccessible(true);
                Class typeField = field.getType();
                switch (typeField.getName()) {
                    case "java.lang.String": {
                        field.set(this.data, (String) value.get(0));
                        break;
                    }
                    case "java.lang.Character": {
                        String valueString = (String) value.get(0);
                        field.set(this.data, valueString.charAt(0));
                        break;
                    }
                    case "java.util.Date": {
                        String valueString = (String)value.get(0);
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = formatter.parse(valueString);
                            field.set(this.data,date);
                        }
                        catch (ParseException ex) {
                            Timestamp timestamp = new Timestamp(Long.parseLong(valueString));
                            field.set(this.data,new Date(timestamp.getTime()));
                        }
                        break;
                    }
                    case "java.math.BigDecimal": {
                        field.set(this.data, new BigDecimal((String) value.get(0)));
                        break;
                    }
                }
                flagContinue = false;
            }
            catch (NoSuchFieldException ex) {
                log.log(Level.WARNING,ex.getMessage());
            }
            catch (Exception ex) {
                flagContinue = false;
                isValid = false;
            }
            if (flagContinue) {
                try {
                    field = this.getClass().getDeclaredField(key);
                    field.setAccessible(true);
                    field.set(this,(String)value.get(0));
                } catch (NoSuchFieldException | IllegalAccessException ex) {
                    log.log(Level.WARNING,ex.getMessage());
                }
            }
        });
        log.log(Level.INFO,"Initialization successful");
    }

    public DBData getData() {
        return data;
    }

    public void setData(DBData data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWebix_operation() {
        return webix_operation;
    }

    public void setWebix_operation(String webix_operation) {
        this.webix_operation = webix_operation;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
