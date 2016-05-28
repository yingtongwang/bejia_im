package www.beijia.com.cn.bejia_im.cache;

import android.text.TextUtils;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import www.beijia.com.cn.bejia_im.util.key.UIKitLogTag;
import www.beijia.com.cn.bejia_im.util.log.LogUtil;

/**
 * Created by Wangyingbao on 2016/5/20.
 */
public class TeamDataCache {

    //---------------单例模式---------------------------
    private static TeamDataCache instance;
    private Map<String, Team> id2TeamMap = new ConcurrentHashMap<String, Team>();

    public static synchronized TeamDataCache getInstance() {
        if (instance == null) {
            instance = new TeamDataCache();
        }
        return instance;
    }

    public void buildCache() {
        List<Team> teams = NIMClient.getService(TeamService.class).queryTeamListBlock();
        addOrUpdateTeam(teams);

        LogUtil.i(UIKitLogTag.TEAM_CACHE, "build TeamDataCache completed, team count = " + teams.size());
    }

    /**
     * 同步从本地获取Team（先从缓存中查询，如果不存在再从SDK DB中查询）
     */
    public Team getTeamById(String teamId) {
        Team team = id2TeamMap.get(teamId);

        if (team == null) {
            team = NIMClient.getService(TeamService.class).queryTeamBlock(teamId);
            addOrUpdateTeam(team);
        }

        return team;
    }

    private void addOrUpdateTeam(List<Team> teamList) {
        if (teamList == null || teamList.isEmpty()) {
            return;
        }

        for (Team t : teamList) {
            if (t == null) {
                continue;
            }

            id2TeamMap.put(t.getId(), t);
        }
    }

    private void addOrUpdateTeam(Team team) {
        if (team == null) {
            return;
        }

        id2TeamMap.put(team.getId(), team);
    }

    public String getTeamNick(String tid, String account) {
        Team team = getTeamById(tid);
        if (team != null && team.getType() == TeamTypeEnum.Advanced) {
            TeamMember member = getTeamMember(tid, account);
            if (member != null && !TextUtils.isEmpty(member.getTeamNick())) {
                return member.getTeamNick();
            }
        }
        return null;
    }

    public void clear() {
        clearTeamCache();
        clearTeamMemberCache();
    }

    /**
     * *
     * ************************************** 群成员缓存(由App主动添加缓存) ****************************************
     */

    private Map<String, Map<String, TeamMember>> teamMemberCache = new ConcurrentHashMap<String, Map<String, TeamMember>>();

    public void clearTeamCache() {
        id2TeamMap.clear();
    }

    public void clearTeamMemberCache() {
        teamMemberCache.clear();
    }

    /**
     * 查询群成员资料（先从缓存中查，如果没有则从SDK DB中查询）
     */
    public TeamMember getTeamMember(String teamId, String account) {
        Map<String, TeamMember> map = teamMemberCache.get(teamId);
        if (map == null) {
            map = new ConcurrentHashMap<String, TeamMember>();
            teamMemberCache.put(teamId, map);
        }

        if (!map.containsKey(account)) {
            TeamMember member = NIMClient.getService(TeamService.class).queryTeamMemberBlock(teamId, account);
            if (member != null) {
                map.put(account, member);
            }
        }

        return map.get(account);
    }

}
