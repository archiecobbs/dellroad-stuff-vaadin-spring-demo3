
package com.example;

import com.vaadin.ui.Table;

import java.util.UUID;

@SuppressWarnings("serial")
public class PetTable extends AbstractTable<PetContainer> {

    public PetTable() {
        super("Pets");
    }

    @Override
    protected PetContainer buildContainer() {
        return new PetContainer();
    }

    @Override
    protected void configureColumns() {
        this.addColumn("name", "Name", 140, Table.Align.LEFT);
        this.addColumn("uuid", "UUID", 260, Table.Align.LEFT);
        this.addColumn("type", "Type", 100, Table.Align.CENTER);
        this.addColumn("birthday", "Birthday", 120, Table.Align.CENTER);
        this.addColumn("friendly", "Friendly?", 100, Table.Align.CENTER);

        this.setColumnExpandRatio("name", 0.60f);
        this.setColumnExpandRatio("type", 0.20f);
        this.setColumnExpandRatio("birthday", 0.25f);
        this.setColumnExpandRatio("friendly", 0.20f);

        this.setColumnCollapsingAllowed(true);
        this.setColumnCollapsed("uuid", true);
    }
}

