package com.javarush.task.task39.task3913;

import com.javarush.task.task39.task3913.query.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogParser implements IPQuery, UserQuery, DateQuery, EventQuery, QLQuery {
    private Path logDir;
    private List<File> logFiles;
    private List<LogEntry> logs;

    public LogParser(Path logDir) {
        this.logDir = logDir;
        logFiles = Arrays.stream(Objects.requireNonNull(logDir.toFile().listFiles()))
                .filter(s->s.getName().endsWith(".log"))
                .collect(Collectors.toList());
        logs = new ArrayList<>();
        logFiles.forEach(this::addLogs);
    }

    @Override
    public int getNumberOfUniqueIPs(Date after, Date before) {
        return getUniqueIPs(after, before).size();
    }

    @Override
    public Set<String> getUniqueIPs(Date after, Date before) {
        Set<String> ips = new LinkedHashSet<>();
        getLogs(after, before).forEach(l -> ips.add(l.ip));
        return ips;
    }

    @Override
    public Set<String> getIPsForUser(String user, Date after, Date before) {
        Set<String> ips = new LinkedHashSet<>();
        getLogs(after, before).stream().filter(l -> l.user.equals(user)).forEach(l -> ips.add(l.ip));
        return ips;
    }

    @Override
    public Set<String> getIPsForEvent(Event event, Date after, Date before) {
        Set<String> ips = new LinkedHashSet<>();
        getLogs(after, before).stream().filter(l -> l.event.equals(event)).forEach(l -> ips.add(l.ip));
        return ips;
    }

    @Override
    public Set<String> getIPsForStatus(Status status, Date after, Date before) {
        Set<String> ips = new LinkedHashSet<>();
        getLogs(after, before).stream().filter(l -> l.status.equals(status)).forEach(l -> ips.add(l.ip));
        return ips;
    }

    @Override
    public Set<String> getAllUsers() {
        return getUsersForOther(null, null, null, null);
    }

    @Override
    public Set<String> getUsersForIP(String ip, Date after, Date before) {
        return getUsersForOther(ip, null, after, before);
    }

    @Override
    public int getNumberOfUsers(Date after, Date before) {
        return getUsersForOther(null, null, after, before).size();
    }

    @Override
    public int getNumberOfUserEvents(String user, Date after, Date before) {
        Set<Event> events = new HashSet<>();
        getLogsForOther(null, user, null, null, after, before).forEach(l -> events.add(l.event));
        return events.size();
    }

    @Override
    public Set<String> getLoggedUsers(Date after, Date before) {
        return getUsersForOther(null, Event.LOGIN, after, before);
    }

    @Override
    public Set<String> getDownloadedPluginUsers(Date after, Date before) {
        return getUsersForOther(null, Event.DOWNLOAD_PLUGIN, after, before);
    }

    @Override
    public Set<String> getWroteMessageUsers(Date after, Date before) {
        return getUsersForOther(null, Event.WRITE_MESSAGE, after, before);
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before) {
        return getUsersForOther(null, Event.SOLVE_TASK, after, before);
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before, int task) {
        return getUsersForTask(Event.SOLVE_TASK, after, before, task);
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before) {
        return getUsersForOther(null, Event.DONE_TASK, after, before);
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before, int task) {
        return getUsersForTask(Event.DONE_TASK, after, before, task);
    }

    @Override
    public Set<Date> getDatesForUserAndEvent(String user, Event event, Date after, Date before) {
        return getDatesForOther(user, event, null, after, before);
    }

    @Override
    public Set<Date> getDatesWhenSomethingFailed(Date after, Date before) {
        return getDatesForOther(null, null, Status.FAILED, after, before);
    }

    @Override
    public Set<Date> getDatesWhenErrorHappened(Date after, Date before) {
        return getDatesForOther(null, null, Status.ERROR, after, before);
    }

    @Override
    public Date getDateWhenUserLoggedFirstTime(String user, Date after, Date before) {
        return getDateWhenUserDoSomethingFirstTime(user, Event.LOGIN, null, after, before);
    }

    @Override
    public Date getDateWhenUserSolvedTask(String user, int task, Date after, Date before) {
        return getDateWhenUserDoSomethingFirstTime(user, Event.SOLVE_TASK, task, after, before);
    }

    @Override
    public Date getDateWhenUserDoneTask(String user, int task, Date after, Date before) {
        return getDateWhenUserDoSomethingFirstTime(user, Event.DONE_TASK, task, after, before);
    }

    @Override
    public Set<Date> getDatesWhenUserWroteMessage(String user, Date after, Date before) {
        return getDatesForOther(user, Event.WRITE_MESSAGE, null, after, before);
    }

    @Override
    public Set<Date> getDatesWhenUserDownloadedPlugin(String user, Date after, Date before) {
        return getDatesForOther(user, Event.DOWNLOAD_PLUGIN, null, after, before);
    }

    @Override
    public int getNumberOfAllEvents(Date after, Date before) {
        return getAllEvents(after, before).size();
    }

    @Override
    public Set<Event> getAllEvents(Date after, Date before) {
        return getEventsForOther(null, null, null, after, before);
    }

    @Override
    public Set<Event> getEventsForIP(String ip, Date after, Date before) {
        return getEventsForOther(ip, null, null, after, before);
    }

    @Override
    public Set<Event> getEventsForUser(String user, Date after, Date before) {
        return getEventsForOther(null, user, null, after, before);
    }

    @Override
    public Set<Event> getFailedEvents(Date after, Date before) {
        return getEventsForOther(null, null, Status.FAILED, after, before);
    }

    @Override
    public Set<Event> getErrorEvents(Date after, Date before) {
        return getEventsForOther(null, null, Status.ERROR, after, before);
    }

    @Override
    public int getNumberOfAttemptToSolveTask(int task, Date after, Date before) {
        return (int) getLogsForOther(null, null, Event.SOLVE_TASK, null, after, before).stream()
                .filter(l -> l.numberTask == task).count();
    }

    @Override
    public int getNumberOfSuccessfulAttemptToSolveTask(int task, Date after, Date before) {
        return (int) getLogsForOther(null, null, Event.DONE_TASK, null, after, before).stream()
                .filter(l -> l.numberTask == task).count();
    }

    @Override
    public Map<Integer, Integer> getAllSolvedTasksAndTheirNumber(Date after, Date before) {
        Map<Integer, Integer> map = new HashMap<>();
        getLogs(after, before).stream()
                .filter(l -> l.event == Event.SOLVE_TASK)
                .forEach(l -> {
                    int count = map.getOrDefault(l.numberTask, 0);
                    map.put(l.numberTask, ++count);
                });
        return map;
    }

    @Override
    public Map<Integer, Integer> getAllDoneTasksAndTheirNumber(Date after, Date before) {
        Map<Integer, Integer> map = new HashMap<>();
        getLogs(after, before).stream()
                .filter(l -> l.event == Event.DONE_TASK)
                .forEach(l -> {
                    int count = map.getOrDefault(l.numberTask, 0);
                    map.put(l.numberTask, ++count);
                });
        return map;
    }

    @Override
    public Set<Object> execute(String query) {
        Set<Object> set = new HashSet<>();
        switch (checkQLQuery(query)) {
            case 1: executeQL(set, query);break;
            case 2: executeQLWithParam(set, query);break;
            case 3: executeQLWithParamAndDates(set, query);break;
        }
        return set;
    }

    /**
     * Выполняет проверку QL-запроса на правильность ввода.
     * Выдаёт номер метода, который необходимо выполнить, в зависимости от сложности запроса.
     * @param query Запрос в виде строки
     * @return Номер метода для обработки QL-запроса
     */
    private int checkQLQuery(String query) {
        if (query == null || query.isEmpty()) return -1;
        if (!query.startsWith("get ")) return -1;

        String[] s = query.split("=");
        String[] lS = s[0].split(" ");
        if (s.length == 1 && lS.length == 2) return 1;

        String[] rS = s[1].split("\"");
        if (lS.length == 4 && lS[2].equals("for")) {
            if (rS.length == 2)
                return 2;
        } else
            return -1;

        if (rS[2].equals(" and date between ") && rS[4].equals(" and "))
            return 3;
        return -1;
    }

    private void executeQL(Set<Object> set, String query) {
        switch (query) {
            case "get ip": set.addAll(getUniqueIPs(null, null));break;
            case "get user": set.addAll(getAllUsers());break;
            case "get date": set.addAll(getDatesForOther(null, null, null, null, null));break;
            case "get event": set.addAll(getAllEvents(null, null));break;
            case "get status":
                Set<Status> statuses = new LinkedHashSet<>();
                getLogsForOther(null, null, null, null, null, null)
                        .forEach(l -> statuses.add(l.status));
                set.addAll(statuses);break;
        }
    }

    private void executeQLWithParam(Set<Object> set, String query) {
        String[] s = query.split("=");
        String[] lS = s[0].split(" ");
        String[] rS = s[1].split("\"");

        String field1 = lS[1];
        String field2 = lS[3];
        String value = rS[1];

        Set<LogEntry> logs = new HashSet<>();
        switch (field2) {
            case "ip":
                logs = getLogsForOther(value, null, null, null, null, null);break;
            case "user":
                logs = getLogsForOther(null, value, null, null, null, null);break;
            case "date":
                logs = getLogsForOther(null, null, null, null, getDate(value), getDate(value));break;
            case "event":
                logs = getLogsForOther(null, null, Event.valueOf(value), null, null, null);break;
            case "status":
                logs = getLogsForOther(null, null, null, Status.valueOf(value), null, null);break;
        }
        switch (field1) {
            case "ip": logs.forEach(l -> set.add(l.ip));break;
            case "user": logs.forEach(l -> set.add(l.user));break;
            case "date": logs.forEach(l -> set.add(l.date));break;
            case "event": logs.forEach(l -> set.add(l.event));break;
            case "status": logs.forEach(l -> set.add(l.status));break;
        }
    }

    private void executeQLWithParamAndDates(Set<Object> set, String query) {
        String[] s = query.split("=");
        String[] lS = s[0].split(" ");
        String[] rS = s[1].split("\"");

        String field1 = lS[1];
        String field2 = lS[3];
        String value = rS[1];
        Date after;
        Date before;
        try {
            after = new Date(getDate(rS[3]).getTime()+1);
        } catch (Exception e) {
            after = null;
        }
        try {
            before = new Date(getDate(rS[5]).getTime()-1);
        } catch (Exception e) {
            before = null;
        }

        if (after != null && before != null && after.after(before)) {
            Date buf = after;
            after = before;
            before = buf;
        }

        Set<LogEntry> logs = new HashSet<>();
        switch (field2) {
            case "ip":
                logs = getLogsForOther(value, null, null, null, after, before);break;
            case "user":
                logs = getLogsForOther(null, value, null, null, after, before);break;
            case "date":
                Set<LogEntry> temp = new HashSet<>();
                if (after != null && before != null && getDate(value).after(after) && getDate(value).before(before) ||
                        after != null && getDate(value).after(after) ||
                        before != null && getDate(value).before(before)) {
                    temp = getLogsForOther(null, null, null, null, after, before);
                }
                temp.stream().filter(l -> l.date.equals(getDate(value))).forEach(logs::add);
                break;
            case "event":
                logs = getLogsForOther(null, null, Event.valueOf(value), null, after, before);break;
            case "status":
                logs = getLogsForOther(null, null, null, Status.valueOf(value), after, before);break;
        }
        switch (field1) {
            case "ip": logs.forEach(l -> set.add(l.ip));break;
            case "user": logs.forEach(l -> set.add(l.user));break;
            case "date": logs.forEach(l -> set.add(l.date));break;
            case "event": logs.forEach(l -> set.add(l.event));break;
            case "status": logs.forEach(l -> set.add(l.status));break;
        }
    }

    private void addLogs(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String s;
            while ((s = reader.readLine()) != null) {
                String[] line = s.split("\t");
                String ip = line[0];
                String user = line[1];
                Date date = getDate(line[2]);
                Event event;
                Status status;
                if (line[3].contains(" ")) {
                    String[] l1 = line[3].split(" ");
                    event = Event.valueOf(l1[0]);
                    int numberTask = Integer.parseInt(l1[1]);
                    status = Status.valueOf(line[4]);
                    logs.add(new LogEntry(ip, user, date, event, numberTask, status));
                } else {
                    event = Event.valueOf(line[3]);
                    status = Status.valueOf(line[4]);
                    logs.add(new LogEntry(ip, user, date, event, status));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Date getDate(String sDate) {
        StringBuilder sb = new StringBuilder();
        String[] s = sDate.split("\\:|\\.| ");
        for (String s1 : s) {
            if (s1.length() == 1) {
                s1 = "0" + s1;
            }
            sb.append(s1);
            sb.append(" ");
        }
        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy HH mm ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(sb.toString());
        } catch (ParseException e) { }
        return date;
    }

    private Set<LogEntry> getLogs(Date after, Date before) {
        Set<LogEntry> logs = new HashSet<>();
        for (LogEntry log : this.logs) {
            if (after != null && before != null) {
                if ((log.date.after(after) || log.date.equals(after)) &&
                        (log.date.before(before) || log.date.equals(before))) {
                    logs.add(log);
                }
            } else if (after == null && before != null) {
                if (log.date.before(before) || log.date.equals(before)) {
                    logs.add(log);
                }
            } else if (after != null) {
                if (log.date.after(after) || log.date.equals(after)) {
                    logs.add(log);
                }
            } else {
                logs.add(log);
            }
        }
        return logs;
    }

    private Set<LogEntry> getLogsForOther(String ip, String user, Event event, Status status, Date after, Date before) {
        Set<LogEntry> logs = new LinkedHashSet<>();
        Stream<LogEntry> stream = getLogs(after, before).stream();
        if (ip != null)
            stream = stream.filter(l -> l.ip.equals(ip));
        if (user != null)
            stream = stream.filter(l -> l.user.equals(user));
        if (event != null)
            stream = stream.filter(l -> l.event == event);
        if (status != null)
            stream = stream.filter(l -> l.status == status);
        stream.forEach(logs::add);
        return logs;
    }

    private Set<String> getUsersForOther(String ip, Event event, Date after, Date before) {
        Set<String> users = new LinkedHashSet<>();
        getLogsForOther(ip, null, event, null, after, before).forEach(l -> users.add(l.user));
        return users;
    }

    private Set<Date> getDatesForOther(String user, Event event, Status status, Date after, Date before) {
        Set<Date> dates = new LinkedHashSet<>();
        getLogsForOther(null, user, event, status, after, before).forEach(l -> dates.add(l.date));
        return dates;
    }

    private Set<Event> getEventsForOther(String ip, String user, Status status, Date after, Date before) {
        Set<Event> events = new LinkedHashSet<>();
        getLogsForOther(ip, user, null, status, after, before).forEach(l -> events.add(l.event));
        return events;
    }

    private Set<String> getUsersForTask(Event event, Date after, Date before, int task) {
        Set<String> users = new HashSet<>();
        getLogsForOther(null, null, event, null, after, before).stream()
                .filter(l -> l.numberTask == task)
                .forEach(l -> users.add(l.user));
        return users;
    }

    private Date getDateWhenUserDoSomethingFirstTime(String user, Event event, Integer task, Date after, Date before) {
        Date date = null;
        Set<Date> dates = new LinkedHashSet<>();
        Stream<LogEntry> stream = getLogsForOther(null, user, event, null, after, before).stream();
        if (task != null)
            stream = stream.filter(l -> l.numberTask == task);
        stream.forEach(l -> dates.add(l.date));
        for (Date d : dates) {
            if (date == null || date.after(d))
                date = d;
        }
        return date;
    }

    private static class LogEntry {
        private String ip;
        private String user;
        private Date date;
        private Event event;
        private int numberTask;
        private Status status;

        public LogEntry(String ip, String user, Date date, Event event, Status status) {
            this.ip = ip;
            this.user = user;
            this.date = date;
            this.event = event;
            this.status = status;
        }

        public LogEntry(String ip, String user, Date date, Event event, int numberTask, Status status) {
            this.ip = ip;
            this.user = user;
            this.date = date;
            this.event = event;
            this.numberTask = numberTask;
            this.status = status;
        }
    }
}