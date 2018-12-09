using DbModel;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Globalization;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;

namespace AdminClient
{
    /// <summary>
    /// View model cho trang tạo môn học
    /// </summary>
    public class CreateSubjectPageViewModel: BaseViewModel
    {
        #region Public Properties

        /// <summary>
        /// View model cho major
        /// </summary>
        public TextEntryViewModel Major { get; set; }

        /// <summary>
        /// View model cho ID
        /// </summary>
        public TextEntryViewModel ID { get; set; }

        /// <summary>
        /// View model cho subject
        /// </summary>
        public TextEntryViewModel Subject { get; set; }
        
        /// <summary>
        /// Số tín chỉ của học phần này
        /// </summary>
        public TextEntryViewModel Credit { get; set; }

        /// <summary>
        /// View model cho teacher
        /// </summary>
        public TextEntryViewModel Teacher { get; set; }

        /// <summary>
        /// Term of this subject
        /// </summary>
        public string Term { get; set; }

        /// <summary>
        /// Course of this subject
        /// </summary>
        public string Course { get; set; }

        /// <summary>
        /// Ngày bắt đầu
        /// </summary>
        public string DateStart { get; set; }

        /// <summary>
        /// Ngày kết thúc
        /// </summary>
        public string DateFinish { get; set; }

        /// <summary>
        /// List giờ để chọn
        /// </summary>
        public ObservableCollection<string> Items { get; set; }

        /// <summary>
        /// List item for term
        /// </summary>
        public ObservableCollection<string> ListTerm { get; set; }

        /// <summary>
        /// List item for course
        /// </summary>
        public ObservableCollection<string> ListCourse { get; set; }

        /// <summary>
        /// Danh sách các thời gian cụ thể trong tuần
        /// </summary>
        public ListTimeOfClassViewModel SpecificTimeItems { get; set; }

        /// <summary>
        /// Biểu thị cho hàm create có đang chạy hay không
        /// </summary>
        public bool CreateIsRunning { get; set; } = false;

        #endregion

        #region Public Commands

        /// <summary>
        /// Lệnh tạo môn học
        /// </summary>
        public ICommand CreateCommand { get; set; }

        

        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public CreateSubjectPageViewModel()
        {
            Items = FunctionHelpersForList.GenerateTimeToChoose();

            Major = new TextEntryViewModel { Label = "Major" };
            ID = new TextEntryViewModel { Label = "ID" };
            Subject = new TextEntryViewModel { Label = "Subject" };
            Credit = new TextEntryViewModel { Label = "Credit" };
            Teacher = new TextEntryViewModel { Label = "Teacher" };

            ListTerm = new ObservableCollection<string>() { "I", "II", "III" };
            ListCourse = FunctionHelpersForList.GenerateCourse(2018, 2050);

            SpecificTimeItems = new ListTimeOfClassViewModel
            {
                Items = new ObservableCollection<TimeOfClassItemViewModel>
                {
                    new TimeOfClassItemViewModel
                    {
                        DayInTheWeek = "Monday",
                    },

                    new TimeOfClassItemViewModel()
                    {
                        DayInTheWeek = "Tuesday",
                    },

                    new TimeOfClassItemViewModel()
                    {
                        DayInTheWeek = "Wednesday",
                    },

                    new TimeOfClassItemViewModel()
                    {
                        DayInTheWeek = "Thursday",
                    },

                    new TimeOfClassItemViewModel()
                    {
                        DayInTheWeek = "Friday",
                    },

                    new TimeOfClassItemViewModel()
                    {
                        DayInTheWeek = "Saturday",
                    },

                    new TimeOfClassItemViewModel()
                    {
                        DayInTheWeek = "Sunday",
                    },
                },
            };

            // Set commands

               CreateCommand = new RelayCommand(async() => await Create());

        }

        #endregion

        #region Command Methods

        /// <summary>
        /// Hàm tạo môn học
        /// </summary>
        public async Task Create()
        {
            await RunCommand(() => this.CreateIsRunning, async () => 
            {
                CreateSubjectCredentialsDataModel DataToPost = ConvertToCredentailsCreate();

                #region Gửi cho server lưu và nhận phản hồi


                // Call the server and attempt to register with credentials
                // TODO: Move all URLs and API routes to static class in core
                var result = await WebRequests.PostAsync<ApiResponse<CreateSubjectResultApiModel>>("http://localhost:51197/api/subject",
                    DataToPost, bearerToken: IoC.ClientDataStore.ApplicationUser.Token);
                // If the response has an error...
                if (result.DisplayErrorIfFailed("Create failed"))
                {
                    // We are done
                    return;
                }

                MessageBox.Show(result.ServerResponse.Response.SuccessfulMessage, "Notify", MessageBoxButton.OK);

                #endregion
            });
        }

        public CreateSubjectCredentialsDataModel ConvertToCredentailsCreate()
        {
            #region Khởi tạo data để gửi 

            CreateSubjectCredentialsDataModel DataToPost = new CreateSubjectCredentialsDataModel();

            DataToPost.Subject = new SubjectDataModel();

            DataToPost.Subject.Major = this.Major.EditText;
            DataToPost.Subject.Id = this.ID.EditText;
            DataToPost.Subject.Subject = this.Subject.EditText;
            DataToPost.Subject.Teacher = this.Teacher.EditText;
            DataToPost.Subject.Term = Term;
            DataToPost.Subject.Course = Course;
            DataToPost.Subject.TimeStart = this.DateStart;
            DataToPost.Subject.TimeFinish = this.DateFinish;
            DataToPost.Subject.Status = 0;

            #endregion

            #region Kiểm tra tín chỉ có phải là số không

            int credit;

            try
            {
                credit = int.Parse(Credit.EditText);
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Credit: {ex.Message}", "Notify", MessageBoxButton.OK, MessageBoxImage.Error);
                return null;
            }

            if (credit < 0)
            {
                MessageBox.Show("Credit must be bigger than zero", "Notify", MessageBoxButton.OK, MessageBoxImage.Error);

                return null;
            }

            DataToPost.Subject.Credit = credit;

            #endregion

            #region Kiểm tra ngày và thời gian bắt đầu và kết thúc

            DateTime dateStart, dateEnd;

            // Kiểm tra xem ngày và thời gian bắt đầu có hợp lệ không
            try
            {

                dateStart = DateTime.ParseExact(DataToPost.Subject.TimeStart, "MM/dd/yyyy", CultureInfo.CurrentCulture);

            }
            catch
            {
                MessageBox.Show("Ngày bắt đầu hoặc thời gian bắt đầu không hợp lệ.\n Mời bạn kiểm tra lại!", "Thông báo",
                    MessageBoxButton.OK, MessageBoxImage.Error);
                return null;
            }

            // Đến đây, ngày và thời gian bắt đầu đã hợp lệ.
            // Tiếp tục kiểm tra xem ngày và thời gian kết thúc có hợp lệ không
            try
            {
                dateEnd = DateTime.ParseExact(DataToPost.Subject.TimeFinish, "MM/dd/yyyy", CultureInfo.CurrentCulture);
            }
            catch
            {
                MessageBox.Show("Ngày kết thúc hoặc thời gian kết thúc không hợp lệ.\n Mời bạn kiểm tra lại!", "Thông báo",
                    MessageBoxButton.OK, MessageBoxImage.Error);
                return null;
            }

            // Đến đây, ngày và thời gian kết thúc đã hợp lệ
            // Tiếp tục kiểm tra xem ngày và thời gian bắt đầu có trước ngày và thời gian kết thúc không.
            // Nếu không thì báo lỗi.
            if (dateStart >= dateEnd)
            {
                MessageBox.Show("Ngày và thời gian bắt đầu phải trước ngày và thời gian kết thúc.\n Mời bạn kiểm tra lại!", "Thông báo",
                   MessageBoxButton.OK, MessageBoxImage.Error);

                return null;
            }

            // Đến đây ngày và thời gian đã hợp lệ là kiểu datetime 
            // Tiếp tục kiểm tra xem ngày và thời gian bắt đầu có sau ngày và thời gian hiện tại không.
            // Nếu không thì báo lỗi.
            if (DateTimeOffset.UtcNow.Date > dateStart)
            {
                MessageBox.Show("Ngày và thời gian bắt đầu phải sau ngày và thời gian hiện tại.\n Mời bạn kiểm tra lại!", "Thông báo",
                  MessageBoxButton.OK, MessageBoxImage.Error);

                return null;
            }

            #endregion

            #region Lấy và kiểm tra lịch trong tuần của môn học

            DataToPost.Schedule = new List<ScheduleDataModel>();

            foreach (var item in SpecificTimeItems.Items)
            {
                if (!item.IsChecked)
                {
                    continue;
                }

                #region Kiểm tra thời gian

                if (!item.TimeStart.IsTime())
                {
                    MessageBox.Show($"Time start of {item.DayInTheWeek} is not in a correct format", "Notify", MessageBoxButton.OK, MessageBoxImage.Error);
                    return null;
                }

                if (!item.TimeFinish.IsTime())
                {
                    MessageBox.Show($"Time finish of {item.DayInTheWeek} is not in a correct format", "Notify", MessageBoxButton.OK, MessageBoxImage.Error);
                    return null;
                }

                DateTime start, finish;
                start = DateTime.ParseExact(item.TimeStart, "HH:mm", CultureInfo.CurrentCulture);
                finish = DateTime.ParseExact(item.TimeFinish, "HH:mm", CultureInfo.CurrentCulture);

                if (start > finish)
                {
                    MessageBox.Show($"{item.DayInTheWeek}: Time start must be before time finish", "Notify", MessageBoxButton.OK, MessageBoxImage.Error);
                    return null;
                }

                #endregion

                DataToPost.Schedule.Add(new ScheduleDataModel
                {
                    Id = this.ID.EditText,
                    DayInTheWeek = item.DayInTheWeek,
                    Room = item.Room,
                    Period = item.Period,
                    TimeStart = item.TimeStart,
                    TimeFinish = item.TimeFinish
                });
            }


            #endregion

            return DataToPost;
        }

        #endregion
    }
}
