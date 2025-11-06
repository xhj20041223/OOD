package view;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

import model.Properties;
import model.Location;
import model.Status;

import controller.IGuiCalendarController;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

/**
 * Graphical User Interface instance.
 *    Contains all GUI components including calendar, event, and day representations.
 */
public class GuiCalendarView implements IGuiCalendarView {
  private JFrame frame;
  private JPanel calendarPanel;
  private JLabel monthLabel;
  private JComboBox<String> calendarDropdown;
  private Map<String, Color> calendars;
  private YearMonth currentMonth;
  private String selectedCalendar;
  private final List<IGuiCalendarController> listeners = new ArrayList<IGuiCalendarController>();
  protected final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

  /**
   * constructs a GuiCalendarView.
   */
  public GuiCalendarView() {
    frame = new JFrame("Calendar App");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500, 500);
    frame.setLayout(new BorderLayout());

    currentMonth = YearMonth.now();
    calendars = new HashMap<>();
    calendars.put("Work", Color.BLUE);
    calendars.put("Personal", Color.GREEN);
    calendars.put("Holidays", Color.RED);
    selectedCalendar = "Work";

    JPanel topPanel = new JPanel();
    JButton prevButton = new JButton("<");
    JButton nextButton = new JButton(">");
    monthLabel = new JLabel();
    calendarDropdown = new JComboBox<>(calendars.keySet().toArray(new String[0]));
    JButton createCalendarButton = new JButton("New Calendar");

    topPanel.add(prevButton);
    topPanel.add(monthLabel);
    topPanel.add(nextButton);
    topPanel.add(calendarDropdown);
    topPanel.add(createCalendarButton);

    frame.add(topPanel, BorderLayout.NORTH);

    calendarPanel = new JPanel();
    frame.add(calendarPanel, BorderLayout.CENTER);

    prevButton.addActionListener(e -> changeMonth(-1));
    nextButton.addActionListener(e -> changeMonth(1));
    calendarDropdown.addActionListener(e -> changeCalendar());
    createCalendarButton.addActionListener(e -> createNewCalendar());

    updateCalendar();
    frame.setVisible(true);
  }

  private void createNewCalendar() {
    String calendarName = JOptionPane.showInputDialog(frame, "Enter calendar name:");
    if (calendarName != null && !calendarName.trim().isEmpty()) {
      String[] timezones = java.util.TimeZone.getAvailableIDs();
      JComboBox<String> timezoneBox = new JComboBox<>(timezones);
      timezoneBox.setSelectedItem("America/New_York");
      int result = JOptionPane.showConfirmDialog(
              frame,
              timezoneBox,
              "Select timezone",
              JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.PLAIN_MESSAGE
      );
      String timezone;
      if (result == JOptionPane.OK_OPTION) {
        timezone = (String) timezoneBox.getSelectedItem();
      } else {
        timezone = "America/New_York";
      }
      for (IGuiCalendarController listener : listeners) {
        listener.createCalendar(calendarName.trim(), timezone.trim());
      }
      calendars.put(calendarName.trim(), getRandomColor());
      calendarDropdown.addItem(calendarName.trim());
      calendarDropdown.setSelectedItem(calendarName.trim());
      selectedCalendar = calendarName.trim();
      updateCalendar();
    }
  }

  /**
   * refreshes the calendar dropdown menu by adding the given calendars.
   * @param calendarNames the calendars to be added.
   */
  public void updateCalendarList(String[] calendarNames) {
    calendarDropdown.removeAllItems();
    for (String name : calendarNames) {
      calendarDropdown.addItem(name);
      if (!calendars.containsKey(name)) {
        calendars.put(name, getRandomColor());
      }
    }
  }

  private Color getRandomColor() {
    Color[] colors = {Color.BLUE, Color.GREEN, Color.RED, Color.ORANGE, Color.MAGENTA, Color.CYAN
            , Color.PINK};
    return colors[(int) (Math.random() * colors.length)];
  }


  private void updateCalendar() {
    calendarPanel.removeAll();
    calendarPanel.setLayout(new GridLayout(0, 7));
    monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());
    calendarPanel.setBackground(calendars.get(selectedCalendar));

    for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
      LocalDate date = currentMonth.atDay(day);
      JButton dayButton = new JButton(String.valueOf(day));
      dayButton.addActionListener(e -> showEvents(date));
      calendarPanel.add(dayButton);
    }


    for (IGuiCalendarController listener : listeners) {
      for (String calendar : calendars.keySet()) {
        try {
          listener.createCalendar(calendar, "America/New_York");
        } catch (Exception ignored) {
        }
      }
      listener.useCalendar(selectedCalendar);
    }
    for (IGuiCalendarController listener : listeners) {
      listener.useCalendar(selectedCalendar);
    }

    frame.revalidate();
    frame.repaint();
  }

  private void changeMonth(int offset) {
    currentMonth = currentMonth.plusMonths(offset);
    updateCalendar();
  }

  private void changeCalendar() {
    selectedCalendar = (String) calendarDropdown.getSelectedItem();
    updateCalendar();
  }

  private void showEvents(LocalDate date) {
    updateCalendar();
    JDialog dialog = new JDialog(frame, "Events", true);
    dialog.setLayout(new BorderLayout());

    JTextArea eventArea = new JTextArea();
    eventArea.setFont(new Font("Default", Font.PLAIN, 20));
    eventArea.setEditable(false);
    StringBuilder events = new StringBuilder();
    for (IGuiCalendarController listener : listeners) {
      events.append(listener.showEvents(date.format(ISO_LOCAL_DATE)));
    }
    if (events.toString().trim().isEmpty()) {
      events.append("No events found on " + date.format(ISO_LOCAL_DATE));
    }
    eventArea.setText(events.toString());
    dialog.add(new JScrollPane(eventArea), BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    JButton createEventButton = new JButton("Create Event");
    JButton editEventButton = new JButton("Edit Event: ");
    buttonPanel.add(createEventButton);
    buttonPanel.add(editEventButton);

    JComboBox<String> event = new JComboBox<>();
    String[] eventStrings = eventArea.getText().split("• ");
    for (String eventString : eventStrings) {
      if (!eventString.trim().isEmpty() && !eventString.trim().startsWith("No events found on ")) {
        event.addItem("• " + eventString.split("\n")[0]);
      }
    }
    editEventButton.setEnabled(event.getSelectedItem() != null);
    buttonPanel.add(event);
    createEventButton.addActionListener(e -> {
      showCreateEventDialog(date);
      StringBuilder newEvents = new StringBuilder();
      for (IGuiCalendarController listener : listeners) {
        newEvents.append(listener.showEvents(date.format(ISO_LOCAL_DATE)));
      }
      if (newEvents.toString().trim().isEmpty()) {
        newEvents.append("No events found on " + date.format(ISO_LOCAL_DATE));
      }
      eventArea.setText(newEvents.toString());
      event.removeAllItems();
      String[] newEventStrings = eventArea.getText().split("• ");
      for (String eventString : newEventStrings) {
        if (!eventString.trim().isEmpty()
                && !eventString.trim().startsWith("No events found on ")) {
          event.addItem("• " + eventString.split("\n")[0]);
        }
      }
      editEventButton.setEnabled(event.getSelectedItem() != null);
    });
    editEventButton.addActionListener(e -> {
      showEditEventDialog(eventArea.getText().split("\n• ")[event.getSelectedIndex()]);
      StringBuilder newEvents = new StringBuilder();
      for (IGuiCalendarController listener : listeners) {
        newEvents.append(listener.showEvents(date.format(ISO_LOCAL_DATE)));
      }
      if (newEvents.toString().trim().isEmpty()) {
        newEvents.append("No events found on " + date.format(ISO_LOCAL_DATE));
      }
      eventArea.setText(newEvents.toString());
      event.removeAllItems();
      String[] newEventStrings = eventArea.getText().split("• ");
      for (String eventString : newEventStrings) {
        if (!eventString.trim().isEmpty()
                && !eventString.trim().startsWith("No events found on ")) {
          event.addItem("• " + eventString.split("\n")[0]);
        }
      }
      editEventButton.setEnabled(event.getSelectedItem() != null);
    });

    dialog.add(buttonPanel, BorderLayout.SOUTH);

    dialog.setSize(800, 600);
    dialog.setLocationRelativeTo(dialog);
    dialog.setVisible(true);
  }

  private void showEditEventDialog(String eventString) {
    String oldSubject = "";
    LocalDateTime oldStart = null;
    LocalDateTime oldEnd = null;
    Status oldStatus = Status.NONSPECIFIC;
    Location oldLocation = Location.NONSPECIFIC;
    String oldDescription = "";

    String[] words = eventString.split("\\s+");

    if (words[2].equals("private")) {
      oldStatus = Status.PRIVATE;
    } else if (words[2].equals("public")) {
      oldStatus = Status.PUBLIC;
    }

    //setting subject
    for (int i = 0; i < words.length - 1; i++) {
      if (words[i].equals("event:")) {
        oldSubject = words[i + 1];
        break;
      }
    }

    //setting start time
    for (int i = 0; i < words.length - 1; i++) {
      if (words[i].equals("from")) {
        oldStart = LocalDateTime.parse(words[i + 1]);
        break;
      }
    }

    //setting end time
    //handling period at the end
    for (int i = 0; i < words.length - 1; i++) {
      if (words[i].equals("to")) {
        String endTimeStr = words[i + 1];
        if (endTimeStr.endsWith(".")) {
          endTimeStr = endTimeStr.substring(0, endTimeStr.length() - 1);
        }
        oldEnd = LocalDateTime.parse(endTimeStr);
        break;
      }
    }

    //location
    //also handling period
    for (int i = 0; i < words.length - 1; i++) {
      if (words[i].equals("to")) {
        String nextWord = words[i + 1];
        if (nextWord.endsWith(".")) {
          nextWord = nextWord.substring(0, nextWord.length() - 1);
        }

        if (i + 2 < words.length && !words[i + 2].equals("Description:")) {
          oldLocation = words[i + 2].startsWith("online") ? Location.ONLINE : Location.PHYSICAL ;
        }
        break;
      }
    }

    //description
    //should be just everything after 'Description: '
    StringBuilder descBuilder = new StringBuilder();
    boolean foundDesc = false;
    for (String word : words) {
      if (foundDesc) {
        if (descBuilder.length() > 0) {
          descBuilder.append(" ");
        }
        descBuilder.append(word);
      } else if (word.equals("Description:")) {
        foundDesc = true;
      }
    }
    if (descBuilder.length() > 0) {
      oldDescription = descBuilder.toString();
      if (oldDescription.endsWith(".")) {
        oldDescription = oldDescription.substring(0, oldDescription.length() - 1);
      }
    }

    final String subject = oldSubject;
    final LocalDateTime start = oldStart;
    final LocalDateTime end = oldEnd;
    final Status status = oldStatus;
    final Location location = oldLocation;
    final String description = oldDescription;


    JDialog dialog = new JDialog(frame, "Edit Event", true);
    dialog.setLayout(new GridBagLayout());
    dialog.setResizable(false);

    JCheckBox subjectCheck = new JCheckBox("Subject");
    JTextField subjectField = new JTextField(20);
    subjectField.setText(subject);
    JCheckBox startTimeCheck = new JCheckBox("Start Time");
    JComboBox<Integer> startYearCombo = createYearCombo();
    JComboBox<Integer> startMonthCombo = createMonthCombo();
    JComboBox<Integer> startDayCombo = createDayCombo();
    JComboBox<Integer> startHourCombo = createHourCombo();
    JComboBox<Integer> startMinuteCombo = createMinuteCombo();

    JCheckBox endTimeCheck = new JCheckBox("End Time");
    JComboBox<Integer> endYearCombo = createYearCombo();
    JComboBox<Integer> endMonthCombo = createMonthCombo();
    JComboBox<Integer> endDayCombo = createDayCombo();
    JComboBox<Integer> endHourCombo = createHourCombo();
    JComboBox<Integer> endMinuteCombo = createMinuteCombo();

    JCheckBox descCheck = new JCheckBox("Description");
    JTextField descField = new JTextField(20);
    descField.setText(description);
    descField.setEnabled(false);

    JCheckBox locationCheck = new JCheckBox("Location");
    JRadioButton onlineRadio = new JRadioButton("Online");
    JRadioButton physicalRadio = new JRadioButton("Physical");
    ButtonGroup locGroup = new ButtonGroup();
    locGroup.add(onlineRadio);
    locGroup.add(physicalRadio);
    onlineRadio.setEnabled(false);
    onlineRadio.setSelected(location.equals(Location.ONLINE));
    physicalRadio.setEnabled(false);
    physicalRadio.setSelected(location.equals(Location.PHYSICAL));

    JCheckBox statusCheck = new JCheckBox("Status");
    JRadioButton privateRadio = new JRadioButton("Private");
    JRadioButton publicRadio = new JRadioButton("Public");
    ButtonGroup statusGroup = new ButtonGroup();
    statusGroup.add(privateRadio);
    statusGroup.add(publicRadio);
    privateRadio.setEnabled(false);
    privateRadio.setSelected(status.equals(Status.PRIVATE));
    publicRadio.setEnabled(false);
    publicRadio.setSelected(status.equals(Status.PUBLIC));

    JButton doEditEventButton = new JButton("Edit Event");


    startYearCombo.setSelectedItem(start.getYear());
    startMonthCombo.setSelectedItem(start.getMonthValue());
    startDayCombo.setSelectedItem(start.getDayOfMonth());
    startHourCombo.setSelectedItem(start.getHour());
    startMinuteCombo.setSelectedItem(start.getMinute());


    endYearCombo.setSelectedItem(end.getYear());
    endMonthCombo.setSelectedItem(end.getMonthValue());
    endDayCombo.setSelectedItem(end.getDayOfMonth());
    endHourCombo.setSelectedItem(end.getHour());
    endMinuteCombo.setSelectedItem(end.getMinute());

    subjectField.setEnabled(false);
    startYearCombo.setEnabled(false);
    startMonthCombo.setEnabled(false);
    startDayCombo.setEnabled(false);
    startHourCombo.setEnabled(false);
    startMinuteCombo.setEnabled(false);
    endYearCombo.setEnabled(false);
    endMonthCombo.setEnabled(false);
    endDayCombo.setEnabled(false);
    endHourCombo.setEnabled(false);
    endMinuteCombo.setEnabled(false);
    descField.setEnabled(false);
    onlineRadio.setEnabled(false);
    physicalRadio.setEnabled(false);
    privateRadio.setEnabled(false);
    publicRadio.setEnabled(false);
    subjectCheck.addActionListener(e -> {
      boolean enabled = subjectCheck.isSelected();
      subjectField.setEnabled(enabled);
    });
    startTimeCheck.addActionListener(e -> {
      boolean enabled = startTimeCheck.isSelected();
      startYearCombo.setEnabled(enabled);
      startMonthCombo.setEnabled(enabled);
      startDayCombo.setEnabled(enabled);
      startHourCombo.setEnabled(enabled);
      startMinuteCombo.setEnabled(enabled);
    });
    endTimeCheck.addActionListener(e -> {
      boolean enabled = endTimeCheck.isSelected();
      endYearCombo.setEnabled(enabled);
      endMonthCombo.setEnabled(enabled);
      endDayCombo.setEnabled(enabled);
      endHourCombo.setEnabled(enabled);
      endMinuteCombo.setEnabled(enabled);
    });
    descCheck.addActionListener(e -> descField.setEnabled(descCheck.isSelected()));
    locationCheck.addActionListener(e -> {
      boolean enabled = locationCheck.isSelected();
      onlineRadio.setEnabled(enabled);
      physicalRadio.setEnabled(enabled);
    });
    statusCheck.addActionListener(e -> {
      boolean enabled = statusCheck.isSelected();
      privateRadio.setEnabled(enabled);
      publicRadio.setEnabled(enabled);
    });

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(4, 8, 4, 8);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 0;

    dialog.add(buildRow(subjectCheck, subjectField), gbc);
    gbc.gridy++;
    dialog.add(buildTimePanel(startTimeCheck, startYearCombo, startMonthCombo, startDayCombo,
            startHourCombo, startMinuteCombo), gbc);
    gbc.gridy++;
    dialog.add(buildTimePanel(endTimeCheck, endYearCombo, endMonthCombo, endDayCombo,
            endHourCombo, endMinuteCombo), gbc);
    gbc.gridy++;
    dialog.add(buildRow(descCheck, descField), gbc);
    gbc.gridy++;
    dialog.add(buildRow(locationCheck, onlineRadio, physicalRadio), gbc);
    gbc.gridy++;
    dialog.add(buildRow(statusCheck, privateRadio, publicRadio), gbc);

    gbc.gridy++;
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(doEditEventButton);
    dialog.add(buttonPanel, gbc);

    doEditEventButton.addActionListener(e -> {
      try {
        if (descCheck.isSelected()) {
          if (descField.getText().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
          }
          for (IGuiCalendarController listener : listeners) {
            listener.editProperty(subject, start.format(FORMATTER), end.format(FORMATTER),
                    Properties.DESCRIPTION, descField.getText());
          }
        }
        if (locationCheck.isSelected()) {
          if (!onlineRadio.isSelected() && !physicalRadio.isSelected()) {
            throw new IllegalArgumentException("please select a location");
          }
          for (IGuiCalendarController listener : listeners) {
            listener.editProperty(subject, start.format(FORMATTER), end.format(FORMATTER),
                    Properties.LOCATION, String.valueOf(onlineRadio.isSelected() ?
                            Location.ONLINE : Location.PHYSICAL));
          }
        }
        if (statusCheck.isSelected()) {
          if (!privateRadio.isSelected() && !publicRadio.isSelected()) {
            throw new IllegalArgumentException("please select status");
          }
          for (IGuiCalendarController listener : listeners) {
            listener.editProperty(subject, start.format(FORMATTER), end.format(FORMATTER),
                    Properties.STATUS, String.valueOf(privateRadio.isSelected() ?
                            Status.PRIVATE : Status.PUBLIC));
          }
        }

        if (subjectCheck.isSelected()) {
          String newSubject = subjectField.getText().trim();
          if (subject.isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be empty");
          }
          for (IGuiCalendarController listener : listeners) {
            listener.editProperty(subject, start.format(FORMATTER), end.format(FORMATTER),
                    Properties.SUBJECT, newSubject);
          }
          if (startTimeCheck.isSelected()) {
            LocalDateTime newStart = getDateTime(startYearCombo, startMonthCombo, startDayCombo,
                    startHourCombo, startMinuteCombo);
            if (endTimeCheck.isSelected()) {
              LocalDateTime newEnd = getDateTime(endYearCombo, endMonthCombo, endDayCombo,
                      endHourCombo, endMinuteCombo);
              if (newStart.isAfter(end)) {
                for (IGuiCalendarController listener : listeners) {
                  listener.editProperty(newSubject, start.format(FORMATTER),
                          end.format(FORMATTER), Properties.END_TIME, newEnd.format(FORMATTER));
                }
                for (IGuiCalendarController listener : listeners) {
                  listener.editProperty(newSubject, start.format(FORMATTER),
                          newEnd.format(FORMATTER), Properties.START_TIME,
                          newStart.format(FORMATTER));
                }
              } else {
                for (IGuiCalendarController listener : listeners) {
                  listener.editProperty(newSubject, start.format(FORMATTER),
                          end.format(FORMATTER), Properties.START_TIME, newStart.format(FORMATTER));
                }
                for (IGuiCalendarController listener : listeners) {
                  listener.editProperty(newSubject, newStart.format(FORMATTER),
                          end.format(FORMATTER), Properties.END_TIME, newEnd.format(FORMATTER));
                }
              }
            }
            else {
              for (IGuiCalendarController listener : listeners) {
                listener.editProperty(newSubject, start.format(FORMATTER),
                        end.format(FORMATTER), Properties.START_TIME, newStart.format(FORMATTER));

              }
            }
          } else if (endTimeCheck.isSelected()) {
            LocalDateTime newEnd = getDateTime(endYearCombo, endMonthCombo, endDayCombo,
                    endHourCombo, endMinuteCombo);
            for (IGuiCalendarController listener : listeners) {
              listener.editProperty(newSubject, start.format(FORMATTER), end.format(FORMATTER),
                      Properties.START_TIME, newEnd.format(FORMATTER));
            }
          }

        } else if (startTimeCheck.isSelected()) {
          LocalDateTime newStart = getDateTime(startYearCombo, startMonthCombo, startDayCombo,
                  startHourCombo, startMinuteCombo);
          if (endTimeCheck.isSelected()) {
            LocalDateTime newEnd = getDateTime(endYearCombo, endMonthCombo, endDayCombo,
                    endHourCombo, endMinuteCombo);
            if (newStart.isAfter(end)) {
              for (IGuiCalendarController listener : listeners) {
                listener.editProperty(subject, start.format(FORMATTER), end.format(FORMATTER),
                        Properties.END_TIME, newEnd.format(FORMATTER));
              }
              for (IGuiCalendarController listener : listeners) {
                listener.editProperty(subject, start.format(FORMATTER), newEnd.format(FORMATTER),
                        Properties.START_TIME, newStart.format(FORMATTER));
              }
            } else {
              for (IGuiCalendarController listener : listeners) {
                listener.editProperty(subject, start.format(FORMATTER), end.format(FORMATTER),
                        Properties.START_TIME, newStart.format(FORMATTER));
              }
              for (IGuiCalendarController listener : listeners) {
                listener.editProperty(subject, newStart.format(FORMATTER), end.format(FORMATTER),
                        Properties.END_TIME, newEnd.format(FORMATTER));
              }
            }
          }
          else {
            for (IGuiCalendarController listener : listeners) {
              listener.editProperty(subject, start.format(FORMATTER),
                      end.format(FORMATTER), Properties.START_TIME, newStart.format(FORMATTER));

            }
          }
        } else if (endTimeCheck.isSelected()) {
          LocalDateTime newEnd = getDateTime(endYearCombo, endMonthCombo, endDayCombo,
                  endHourCombo, endMinuteCombo);
          for (IGuiCalendarController listener : listeners) {
            listener.editProperty(subject, start.format(FORMATTER), end.format(FORMATTER),
                    Properties.END_TIME, newEnd.format(FORMATTER));
          }
        }

        JOptionPane.showMessageDialog(dialog, "Event has been edited", "Success!",
                JOptionPane.INFORMATION_MESSAGE);
        dialog.dispose();
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Fail",
                JOptionPane.ERROR_MESSAGE);
      }
    });

    dialog.pack();
    dialog.setLocationRelativeTo(frame);
    dialog.setVisible(true);
  }

  private void showCreateEventDialog(LocalDate date) {
    JDialog dialog = new JDialog(frame, "Create Event", true);
    dialog.setLayout(new GridBagLayout());
    dialog.setResizable(false);

    JTextField subjectField = new JTextField(20);
    JComboBox<Integer> startYearCombo = createYearCombo();
    JComboBox<Integer> startMonthCombo = createMonthCombo();
    JComboBox<Integer> startDayCombo = createDayCombo();
    JComboBox<Integer> startHourCombo = createHourCombo();
    JComboBox<Integer> startMinuteCombo = createMinuteCombo();

    JCheckBox endTimeCheck = new JCheckBox("End Time");
    JComboBox<Integer> endYearCombo = createYearCombo();
    JComboBox<Integer> endMonthCombo = createMonthCombo();
    JComboBox<Integer> endDayCombo = createDayCombo();
    JComboBox<Integer> endHourCombo = createHourCombo();
    JComboBox<Integer> endMinuteCombo = createMinuteCombo();

    JCheckBox descCheck = new JCheckBox("Description");
    JTextField descField = new JTextField(20);
    descField.setEnabled(false);

    JCheckBox locationCheck = new JCheckBox("Location");
    JRadioButton onlineRadio = new JRadioButton("Online");
    JRadioButton physicalRadio = new JRadioButton("Physical");
    ButtonGroup locGroup = new ButtonGroup();
    locGroup.add(onlineRadio);
    locGroup.add(physicalRadio);
    onlineRadio.setEnabled(false);
    physicalRadio.setEnabled(false);

    JCheckBox statusCheck = new JCheckBox("Status");
    JRadioButton privateRadio = new JRadioButton("Private");
    JRadioButton publicRadio = new JRadioButton("Public");
    ButtonGroup statusGroup = new ButtonGroup();
    statusGroup.add(privateRadio);
    statusGroup.add(publicRadio);
    privateRadio.setEnabled(false);
    publicRadio.setEnabled(false);

    JCheckBox seriesCheck = new JCheckBox("Is Event Series");
    JCheckBox[] weekDayChecks = new JCheckBox[7];
    String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    for (int i = 0; i < 7; ++i) {
      weekDayChecks[i] = new JCheckBox(days[i]);
    }
    JRadioButton untilDateRadio = new JRadioButton("End by Date");
    JRadioButton untilCountRadio = new JRadioButton("Repeat N Times");
    ButtonGroup seriesEndGroup = new ButtonGroup();
    seriesEndGroup.add(untilDateRadio);
    seriesEndGroup.add(untilCountRadio);
    JComboBox<Integer> seriesEndYearCombo = createYearCombo();
    JComboBox<Integer> seriesEndMonthCombo = createMonthCombo();
    JComboBox<Integer> seriesEndDayCombo = createDayCombo();
    JTextField repeatCountField = new JTextField(5);

    JButton doCreateEventButton = new JButton("Create Event");


    startYearCombo.setSelectedItem(date.getYear());
    startMonthCombo.setSelectedItem(date.getMonthValue());
    startDayCombo.setSelectedItem(date.getDayOfMonth());
    startHourCombo.setSelectedItem(8);
    startMinuteCombo.setSelectedItem(0);


    endYearCombo.setSelectedItem(date.getYear());
    endMonthCombo.setSelectedItem(date.getMonthValue());
    endDayCombo.setSelectedItem(date.getDayOfMonth());
    endHourCombo.setSelectedItem(17);
    endMinuteCombo.setSelectedItem(0);

    seriesEndYearCombo.setSelectedItem(date.getYear());
    seriesEndMonthCombo.setSelectedItem(date.getMonthValue() + 1);
    seriesEndDayCombo.setSelectedItem(date.getDayOfMonth());

    endYearCombo.setEnabled(false);
    endMonthCombo.setEnabled(false);
    endDayCombo.setEnabled(false);
    endHourCombo.setEnabled(false);
    endMinuteCombo.setEnabled(false);
    descField.setEnabled(false);
    onlineRadio.setEnabled(false);
    physicalRadio.setEnabled(false);
    privateRadio.setEnabled(false);
    publicRadio.setEnabled(false);
    for (JCheckBox cb : weekDayChecks) {
      cb.setEnabled(false);
    }
    untilDateRadio.setEnabled(false);
    untilCountRadio.setEnabled(false);
    seriesEndYearCombo.setEnabled(false);
    seriesEndMonthCombo.setEnabled(false);
    seriesEndDayCombo.setEnabled(false);
    repeatCountField.setEnabled(false);

    endTimeCheck.addActionListener(e -> {
      boolean enabled = endTimeCheck.isSelected();
      endYearCombo.setEnabled(enabled);
      endMonthCombo.setEnabled(enabled);
      endDayCombo.setEnabled(enabled);
      endHourCombo.setEnabled(enabled);
      endMinuteCombo.setEnabled(enabled);
    });
    descCheck.addActionListener(e -> descField.setEnabled(descCheck.isSelected()));
    locationCheck.addActionListener(e -> {
      boolean enabled = locationCheck.isSelected();
      onlineRadio.setEnabled(enabled);
      physicalRadio.setEnabled(enabled);
    });
    statusCheck.addActionListener(e -> {
      boolean enabled = statusCheck.isSelected();
      privateRadio.setEnabled(enabled);
      publicRadio.setEnabled(enabled);
    });
    seriesCheck.addActionListener(e -> {
      boolean enabled = seriesCheck.isSelected();
      for (JCheckBox cb : weekDayChecks) {
        cb.setEnabled(enabled);
      }
      untilDateRadio.setEnabled(enabled);
      untilCountRadio.setEnabled(enabled);
      if (!enabled) {
        seriesEndYearCombo.setEnabled(false);
        seriesEndMonthCombo.setEnabled(false);
        seriesEndDayCombo.setEnabled(false);
        repeatCountField.setEnabled(false);
      }
    });
    untilDateRadio.addActionListener(e -> {
      boolean enabled = seriesCheck.isSelected() && untilDateRadio.isSelected();
      seriesEndYearCombo.setEnabled(enabled);
      seriesEndMonthCombo.setEnabled(enabled);
      seriesEndDayCombo.setEnabled(enabled);
      repeatCountField.setEnabled(false);
    });
    untilCountRadio.addActionListener(e -> {
      boolean enabled = seriesCheck.isSelected() && untilCountRadio.isSelected();
      repeatCountField.setEnabled(enabled);
      seriesEndYearCombo.setEnabled(false);
      seriesEndMonthCombo.setEnabled(false);
      seriesEndDayCombo.setEnabled(false);
    });

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(4, 8, 4, 8);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 0;

    dialog.add(buildRow("Subject:", subjectField), gbc);
    gbc.gridy++;
    dialog.add(buildTimePanel("Start Time:", startYearCombo, startMonthCombo, startDayCombo,
            startHourCombo, startMinuteCombo), gbc);
    gbc.gridy++;
    dialog.add(buildTimePanel(endTimeCheck, endYearCombo, endMonthCombo, endDayCombo,
            endHourCombo, endMinuteCombo), gbc);
    gbc.gridy++;
    dialog.add(buildRow(descCheck, descField), gbc);
    gbc.gridy++;
    dialog.add(buildRow(locationCheck, onlineRadio, physicalRadio), gbc);
    gbc.gridy++;
    dialog.add(buildRow(statusCheck, privateRadio, publicRadio), gbc);

    gbc.gridy++;
    JPanel seriesPanel = new JPanel();
    seriesPanel.setLayout(new BoxLayout(seriesPanel, BoxLayout.Y_AXIS));
    JPanel weekPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    for (JCheckBox cb : weekDayChecks) {
      weekPanel.add(cb);
    }
    JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    optionPanel.add(untilDateRadio);
    optionPanel.add(seriesEndYearCombo);
    optionPanel.add(new JLabel("Year"));
    optionPanel.add(seriesEndMonthCombo);
    optionPanel.add(new JLabel("Month"));
    optionPanel.add(seriesEndDayCombo);
    optionPanel.add(new JLabel("Day"));
    optionPanel.add(untilCountRadio);
    optionPanel.add(new JLabel("Repeat:"));
    optionPanel.add(repeatCountField);

    JPanel seriesHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
    seriesHeader.add(seriesCheck);

    seriesPanel.add(seriesHeader);
    seriesPanel.add(weekPanel);
    seriesPanel.add(optionPanel);
    dialog.add(seriesPanel, gbc);

    gbc.gridy++;
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(doCreateEventButton);
    dialog.add(buttonPanel, gbc);

    doCreateEventButton.addActionListener(e -> {
      try {
        String subject = subjectField.getText().trim();
        if (subject.isEmpty()) {
          throw new IllegalArgumentException("Subject cannot be empty");
        }
        LocalDateTime startDateTime = getDateTime(startYearCombo, startMonthCombo, startDayCombo,
                startHourCombo, startMinuteCombo);

        HashMap<Properties, String> properties = new HashMap<>();
        if (endTimeCheck.isSelected()) {
          LocalDateTime endDateTime = getDateTime(endYearCombo, endMonthCombo, endDayCombo,
                  endHourCombo, endMinuteCombo);
          if (!endDateTime.isAfter(startDateTime)) {
            throw new IllegalArgumentException("End Time must be later than Start Time");
          }
          properties.put(Properties.END_TIME, endDateTime.format(FORMATTER));
        } else {
          startDateTime = LocalDateTime.of(startDateTime.toLocalDate(),
                  LocalTime.of(8, 0));
          LocalDateTime endDateTime = LocalDateTime.of(startDateTime.toLocalDate(),
                  LocalTime.of(17, 0));
          properties.put(Properties.END_TIME, endDateTime.format(FORMATTER));
        }
        if (descCheck.isSelected()) {
          if (descField.getText().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
          }
          properties.put(Properties.DESCRIPTION, descField.getText());
        }
        if (locationCheck.isSelected()) {
          if (!onlineRadio.isSelected() && !physicalRadio.isSelected()) {
            throw new IllegalArgumentException("please select location");
          }
          properties.put(Properties.LOCATION, String.valueOf(onlineRadio.isSelected()
                  ? Location.ONLINE : Location.PHYSICAL));
        }
        if (statusCheck.isSelected()) {
          if (!privateRadio.isSelected() && !publicRadio.isSelected()) {
            throw new IllegalArgumentException("please select status");
          }
          properties.put(Properties.STATUS, String.valueOf(privateRadio.isSelected()
                  ? Status.PRIVATE : Status.PUBLIC));
        }

        if (seriesCheck.isSelected()) {
          List<Integer> selectedDays = new ArrayList<>();
          for (int i = 0; i < 7; ++i) {
            if (weekDayChecks[i].isSelected()) {
              selectedDays.add(i + 1);
            }
          }
          if (selectedDays.isEmpty()) {
            throw new IllegalArgumentException("Please choose at least 1 weekday");
          }
          String weekdays = convertWeekDay(selectedDays);

          if (untilDateRadio.isSelected()) {
            LocalDate seriesEndDate = getDate(seriesEndYearCombo, seriesEndMonthCombo,
                    seriesEndDayCombo);
            if (seriesEndDate.isBefore(startDateTime.toLocalDate())) {
              throw new IllegalArgumentException("Series need to end after Start Time");
            }
            for (IGuiCalendarController listener : listeners) {
              listener.createEventSeries(subject, startDateTime.format(FORMATTER), properties,
                      weekdays,
                      seriesEndDate.format(ISO_LOCAL_DATE));
            }
          } else if (untilCountRadio.isSelected()) {
            int repeatCount;
            try {
              repeatCount = Integer.parseInt(repeatCountField.getText().trim());
            } catch (Exception ex) {
              throw new IllegalArgumentException("Repeat need to be an positive integer");
            }
            if (repeatCount <= 0) {
              throw new IllegalArgumentException("Repeat need to be an positive integer");
            }
            for (IGuiCalendarController listener : listeners) {
              listener.createEventSeries(subject, startDateTime.format(FORMATTER), properties,
                      weekdays,
                      repeatCount);
            }
          } else {
            throw new IllegalArgumentException("Please choose how to end series");
          }
        } else {
          for (IGuiCalendarController listener : listeners) {
            listener.createEvent(subject, startDateTime.format(FORMATTER), properties);
          }
        }
        JOptionPane.showMessageDialog(dialog, "Event has been created", "Success!",
                JOptionPane.INFORMATION_MESSAGE);
        dialog.dispose();
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Fail",
                JOptionPane.ERROR_MESSAGE);
      }
    });

    dialog.pack();
    dialog.setLocationRelativeTo(frame);
    dialog.setVisible(true);
  }

  private JComboBox<Integer> createYearCombo() {
    JComboBox<Integer> cb = new JComboBox<>();
    int thisYear = LocalDate.now().getYear();
    for (int y = thisYear - 10; y <= thisYear + 10; ++y) {
      cb.addItem(y);
    }
    return cb;
  }

  private JComboBox<Integer> createMonthCombo() {
    JComboBox<Integer> cb = new JComboBox<>();
    for (int m = 1; m <= 12; ++m) {
      cb.addItem(m);
    }
    return cb;
  }

  private JComboBox<Integer> createDayCombo() {
    JComboBox<Integer> cb = new JComboBox<>();
    for (int d = 1; d <= 31; ++d) {
      cb.addItem(d);
    }
    return cb;
  }

  private JComboBox<Integer> createHourCombo() {
    JComboBox<Integer> cb = new JComboBox<>();
    for (int h = 0; h <= 23; ++h) {
      cb.addItem(h);
    }
    return cb;
  }

  private JComboBox<Integer> createMinuteCombo() {
    JComboBox<Integer> cb = new JComboBox<>();
    for (int m = 0; m < 60; ++m) {
      cb.addItem(m);
    }
    return cb;
  }

  private JPanel buildRow(Object... comps) {
    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    for (Object o : comps) {
      if (o instanceof Component) {
        p.add((Component) o);
      } else if (o instanceof String) {
        p.add(new JLabel((String) o));
      }
    }
    return p;
  }

  private JPanel buildTimePanel(Object first, JComboBox<Integer> year, JComboBox<Integer> month,
                                JComboBox<Integer> day, JComboBox<Integer> hour,
                                JComboBox<Integer> minute) {
    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    if (first instanceof String) {
      p.add(new JLabel((String) first));
    } else {
      p.add((Component) first);
    }
    p.add(year);
    p.add(new JLabel("Year"));
    p.add(month);
    p.add(new JLabel("Month"));
    p.add(day);
    p.add(new JLabel("Day"));
    p.add(hour);
    p.add(new JLabel("Hour"));
    p.add(minute);
    p.add(new JLabel("Minute"));
    return p;
  }

  private LocalDateTime getDateTime(JComboBox<Integer> year, JComboBox<Integer> month,
                                    JComboBox<Integer> day,
                                    JComboBox<Integer> hour, JComboBox<Integer> minute) {
    int y = (Integer) year.getSelectedItem();
    int m = (Integer) month.getSelectedItem();
    int d = (Integer) day.getSelectedItem();
    int h = (Integer) hour.getSelectedItem();
    int min = (Integer) minute.getSelectedItem();
    return LocalDateTime.of(y, m, d, h, min);
  }

  private LocalDate getDate(JComboBox<Integer> year, JComboBox<Integer> month,
                            JComboBox<Integer> day) {
    int y = (Integer) year.getSelectedItem();
    int m = (Integer) month.getSelectedItem();
    int d = (Integer) day.getSelectedItem();
    return LocalDate.of(y, m, d);
  }

  private String convertWeekDay(List<Integer> weekDay) {
    StringBuilder days = new StringBuilder();
    for (Integer i : weekDay) {
      switch (i) {
        case 1:
          days.append("M");
          break;
        case 2:
          days.append("T");
          break;
        case 3:
          days.append("W");
          break;
        case 4:
          days.append("R");
          break;
        case 5:
          days.append("F");
          break;
        case 6:
          days.append("S");
          break;
        case 7:
          days.append("U");
          break;
        default:
          break;
      }
    }
    return days.toString();
  }

  /**
   * adds a GUIController as a listener to this view.
   * @param calendarController the GUIController to be added as a listener
   */
  @Override
  public void addListener(IGuiCalendarController calendarController) {
    this.listeners.add(calendarController);
  }
}
