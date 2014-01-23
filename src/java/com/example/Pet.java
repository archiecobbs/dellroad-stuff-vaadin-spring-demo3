
package com.example;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.dellroad.stuff.vaadin7.FieldBuilder;
import org.dellroad.stuff.vaadin7.ProvidesProperty;
import org.dellroad.stuff.vaadin7.ProvidesPropertySort;
import org.dellroad.stuff.validation.Unique;

/*

  Notes:

  - The @ProvidesProperty and @ProvidesProperty annotations are used to build a Container
    whose items reflect instances of this class; see PetContainer.

  - The @FieldBuilder annotations is used to build Field objects for editing instances of this class; see PetPanel.

*/

public class Pet implements HasUUID {

    private UUID uuid = UUID.randomUUID();
    private String name;
    private Date birthday;
    private boolean friendly;
    private PetType type;

    @NotNull
    @Override
    @Unique(domain = "uuid")
    public UUID getUUID() {
        return this.uuid;
    }
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @FieldBuilder.AbstractField(caption = "Name:", width = "200px")
    @FieldBuilder.AbstractTextField(nullRepresentation = "", nullSettingAllowed = true)
    @NotNull(message = "Pet must have a name")
    @Pattern(regexp = "[A-Z].*", message = "Pet name must start with a capital letter")
    @ProvidesProperty
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @FieldBuilder.AbstractField(caption = "Type:")
    @FieldBuilder.EnumComboBox(enumClass = PetType.class)
    @NotNull(message = "Pet must have a type")
    @ProvidesProperty
    public PetType getType() {
        return this.type;
    }
    public void setType(PetType type) {
        this.type = type;
    }

    @FieldBuilder.AbstractField(caption = "Birthday:")
    @FieldBuilder.DateField(resolution = Resolution.DAY)
    @ProvidesPropertySort
    public Date getBirthday() {
        return this.birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @FieldBuilder.AbstractField(caption = "Pet is friendly")
    @FieldBuilder.CheckBox
    public boolean isFriendly() {
        return this.friendly;
    }
    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
    }

// Additional Vaadin property configuration

    @ProvidesProperty("uuid")
    public Label propertyUUID() {
        return new Label("<code>" + this.uuid.toString() + "</code>", ContentMode.HTML);
    }

    @ProvidesProperty("birthday")
    private String propertyBirthday() {
        if (this.birthday == null)
            return "Unknown";
        return new SimpleDateFormat("yyyy-MM-dd").format(this.birthday);
    }

    @ProvidesProperty("friendly")
    private String propertyFriendly() {
        return this.friendly ? "Yes" : "No";
    }
}

