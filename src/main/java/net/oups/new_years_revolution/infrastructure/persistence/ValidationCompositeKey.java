package net.oups.new_years_revolution.infrastructure.persistence;

import java.io.Serializable;
import java.util.Date;

public class ValidationCompositeKey implements Serializable {
    private Resolution resolution;
    private Account creator;
    private Date date;
}
