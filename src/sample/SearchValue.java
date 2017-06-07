package sample;

import javafx.beans.property.SimpleStringProperty;

public class SearchValue {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchValue)) return false;

        SearchValue that = (SearchValue) o;

        if (getLeader() != null ? !getLeader().equals(that.getLeader()) : that.getLeader() != null) return false;
        if (getCountValue() != null ? !getCountValue().equals(that.getCountValue()) : that.getCountValue() != null)
            return false;
        if (getSearchValue() != null ? !getSearchValue().equals(that.getSearchValue()) : that.getSearchValue() != null)
            return false;
        return getEditionValue() != null ? getEditionValue().equals(that.getEditionValue()) : that.getEditionValue() == null;
    }

    @Override
    public int hashCode() {
        int result = getLeader() != null ? getLeader().hashCode() : 0;
        result = 31 * result + (getCountValue() != null ? getCountValue().hashCode() : 0);
        result = 31 * result + (getSearchValue() != null ? getSearchValue().hashCode() : 0);
        result = 31 * result + (getEditionValue() != null ? getEditionValue().hashCode() : 0);
        return result;
    }

    public String getLeader() {

        return leader.get();
    }

    public SimpleStringProperty leaderProperty() {
        return leader;
    }

    private final SimpleStringProperty leader;
    private final SimpleStringProperty countValue;
    private final SimpleStringProperty searchValue;

    public String getEditionValue() {
        return editionValue.get();
    }

    public SimpleStringProperty editionValueProperty() {
        return editionValue;
    }

    private final SimpleStringProperty editionValue;


    SearchValue(String lead, String  countVal, String searchVal, String edition) {
        this.leader = new SimpleStringProperty(lead);
        this.countValue = new SimpleStringProperty(countVal);
        this.searchValue = new SimpleStringProperty(searchVal);
        this.editionValue = new SimpleStringProperty(edition);

    }


    public String getCountValue() {
        return countValue.get();
    }

    public void setCountValue(String countVal) {
        countValue.set(countVal);
    }

    public String getSearchValue() {
        return searchValue.get();
    }

    public void setSearchValue(String searchVal) {
        searchValue.set(searchVal);
    }

}
