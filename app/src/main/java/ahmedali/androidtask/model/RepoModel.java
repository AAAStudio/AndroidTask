package ahmedali.androidtask.model;

public class RepoModel {

    public int repoId;
    public String ownerName,repoName,repoDesc,ownerUrl,repoUrl;
    public boolean fork;

    private boolean isForkMissing;

    public RepoModel(int repoId, String ownerName, String repoName, String repoDesc, String ownerUrl, String repoUrl,boolean fork) {
        this.repoId = repoId;
        this.ownerName = ownerName;
        this.repoName = repoName;
        this.repoDesc = repoDesc;
        this.ownerUrl = ownerUrl;
        this.repoUrl = repoUrl;
        this.fork = fork;
    }

    public boolean isForkMissing() {
        return isForkMissing;
    }

    public void setForkMissing(boolean forkMissing) {
        isForkMissing = forkMissing;
    }
}
