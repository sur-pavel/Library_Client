package ru.sur_pavel.Library_Client.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Class of objects for searchTable
 */
public class SearchValue {

    private final SimpleStringProperty leader;
    private final SimpleStringProperty countValue;
    private final SimpleStringProperty searchValue;

    public SearchValue(String lead, String countVal, String searchVal) {
        this.leader = new SimpleStringProperty(lead);
        this.countValue = new SimpleStringProperty(countVal);
        this.searchValue = new SimpleStringProperty(searchVal);
    }

    public String getLeader() {
        return leader.get();
    }

    public SimpleStringProperty leaderProperty() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader.set(leader);
    }

    public String getCountValue() {
        return countValue.get();
    }

    public SimpleStringProperty countValueProperty() {
        return countValue;
    }

    public void setCountValue(String countValue) {
        this.countValue.set(countValue);
    }

    public String getSearchValue() {
        return searchValue.get();
    }

    public SimpleStringProperty searchValueProperty() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue.set(searchValue);
    }

    public String toString(){
        return getLeader() + getCountValue() + getSearchValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchValue)) return false;

        SearchValue that = (SearchValue) o;

        if (getLeader() != null ? !getLeader().equals(that.getLeader()) : that.getLeader() != null) return false;
        if (getCountValue() != null ? !getCountValue().equals(that.getCountValue()) : that.getCountValue() != null)
            return false;
        return getSearchValue() != null ? !getSearchValue().equals(that.getSearchValue()) : that.getSearchValue() != null;
    }

    @Override
    public int hashCode() {
        int result = getLeader() != null ? getLeader().hashCode() : 0;
        result = 31 * result + (getCountValue() != null ? getCountValue().hashCode() : 0);
        result = 31 * result + (getSearchValue() != null ? getSearchValue().hashCode() : 0);
        return result;
    }
}
