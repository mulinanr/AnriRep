package de.hhu.cs.dbs.internship.project.gui;

import com.alexanderthelen.applicationkit.database.Table;
import com.alexanderthelen.applicationkit.gui.TableViewController;
import com.alexanderthelen.applicationkit.gui.ViewController;
import de.hhu.cs.dbs.internship.project.table.*;
import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.util.ArrayList;

public class MasterViewController extends com.alexanderthelen.applicationkit.gui.MasterViewController {
    protected MasterViewController(String name) {
        super(name);
    }

    public static MasterViewController createWithName(String name) throws IOException {
        MasterViewController controller = new MasterViewController(name);
        controller.loadView();
        return controller;
    }

    @Override
    protected ArrayList<TreeItem<ViewController>> getTreeItems() {
        ArrayList<TreeItem<ViewController>> treeItems = new ArrayList<>();
        TreeItem<ViewController> treeItem;
        TreeItem<ViewController> subTreeItem;

        treeItem = getTreeItem(new MitarbeiterProfile(), "Mitarbeiter Profile", "mitarbeiter-profile", "Mitarbeiter Profile");
        treeItem.setExpanded(true);
        treeItems.add(treeItem);


        treeItem = getTreeItem(new Kunde(), "Kunde", "kunde", "Kunde");
        treeItem.setExpanded(true);
        treeItems.add(treeItem);

        subTreeItem = getTreeItem(new Konto(), "Konto", "konto", "Konto");
        treeItem.getChildren().add(subTreeItem);



        treeItem = getTreeItem(new Kontakteintrag(), "Kontakteintrag", "kontakteintrag", "Kontakteintrag");
        treeItem.setExpanded(true);
        treeItems.add(treeItem);

        subTreeItem = getTreeItem(new Adresse(), "Adresse", "adresse", "Adresse");
        treeItem.getChildren().add(subTreeItem);




        treeItem = getTreeItem(new Medikament(), "Medikament", "medikament", "Medikament");
        treeItem.setExpanded(true);
        treeItems.add(treeItem);

        subTreeItem = getTreeItem(new AuftragBuchtMedikament(), "Gebuchte Medikamente", "auftraege-medikament", "Gebuchte Medikamente");
        treeItem.getChildren().add(subTreeItem);

        subTreeItem = getTreeItem(new Staffelpreis(), "Staffelpreis", "staffelpreis", "Staffelpreis");
        treeItem.getChildren().add(subTreeItem);



        treeItem = getTreeItem(new Auftrag(), "Aufträge", "auftraege", "Aufträge");
        treeItem.setExpanded(true);
        treeItems.add(treeItem);

        subTreeItem = getTreeItem(new AuftragKunden(), "Aufträge eines Kunden", "auftraege-kunden", "Aufträge eines Kunden");
        treeItem.getChildren().add(subTreeItem);

        subTreeItem = getTreeItem(new AuftragMedikament(), "Aufträge mit Medikamenten", "auftraege-medikament", "Aufträge mit Medikamenten");
        treeItem.getChildren().add(subTreeItem);

        subTreeItem = getTreeItem(new AuftraegeErstellungsdatum(), "Aufträge nach Erstellungsdatum", "auftraege-erstellungsdatum", "Aufträge nach Erstellungsdatum");
        treeItem.getChildren().add(subTreeItem);

        subTreeItem = getTreeItem(new AuftraegeLieferdatum(), "Aufträge nach Lieferdatum", "auftraege-lieferdatum", "Aufträge nach Lieferdatum");
        treeItem.getChildren().add(subTreeItem);

        subTreeItem = getTreeItem(new AuftragTotalPreis(), "Auftrag Total Preis", "auftrag-total-preis", "Auftrag Total Preis");
        treeItem.getChildren().add(subTreeItem);






        /*
        table = new Account();
        table.setTitle("Account");
        try {
            tableViewController = TableViewController.createWithNameAndTable("account", table);
            tableViewController.setTitle("Account");
        } catch (IOException e) {
            tableViewController = null;
        }
        treeItem = new TreeItem<>(tableViewController);
        treeItem.setExpanded(true);
        treeItems.add(treeItem);

        table = new Favorites();
        table.setTitle("Favoriten");
        try {
            tableViewController = TableViewController.createWithNameAndTable("favorites", table);
            tableViewController.setTitle("Favoriten");
        } catch (IOException e) {
            tableViewController = null;
        }
        subTreeItem = new TreeItem<>(tableViewController);
        treeItem.getChildren().add(subTreeItem);
        */

        return treeItems;
    }

    private TreeItem<ViewController> getTreeItem(Table table, String tableTitle, String controllerName, String controllerTitle) {
        TableViewController tableViewController = null;

        table.setTitle(tableTitle);
        try {
            tableViewController = TableViewController.createWithNameAndTable(controllerName, table);
            tableViewController.setTitle(controllerTitle);
        } catch (IOException e) {
            tableViewController = null;
        }
        TreeItem<ViewController> treeItem = new TreeItem<>(tableViewController);
        return treeItem;
    }
}
