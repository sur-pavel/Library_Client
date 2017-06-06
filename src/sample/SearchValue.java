package sample;

import javafx.beans.property.SimpleStringProperty;

public class SearchValue {

//    private final SimpleIntegerProperty countValue;
    private final SimpleStringProperty searchValue;


    SearchValue(String searchVal) {
//        this.countValue = new SimpleIntegerProperty(countVal);
        this.searchValue = new SimpleStringProperty(searchVal);

    }

//    public Integer getCountValue() {
//        return countValue.get();
//    }

//    public void setCountValue(Integer countVal) {
//        countValue.set(countVal);
//    }

    public String getSearchValue() {
        return searchValue.get();
    }

    public void setSearchValue(String searchVal) {
        searchValue.set(searchVal);
    }

}
