using DbModel;
using System;
using System.Collections.ObjectModel;
using System.Globalization;
using System.Threading.Tasks;
using System.Windows;
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
        /// View model cho teacher
        /// </summary>
        public TextEntryViewModel Teacher { get; set; }

        /// <summary>
        /// View model cho class
        /// </summary>
        public TextEntryViewModel Class { get; set; }

        /// <summary>
        /// Ngày bắt đầu
        /// </summary>
        public string DateStart { get; set; }

        /// <summary>
        /// Giờ bắt đầu
        /// </summary>
        public string TimeStart { get; set; }

        /// <summary>
        /// Ngày kết thúc
        /// </summary>
        public string DateFinish { get; set; }

        /// <summary>
        /// Giờ kết thúc
        /// </summary>
        public string TimeFinish { get; set; }

        /// <summary>
        /// List giờ để chọn
        /// </summary>
        public ObservableCollection<string> Items { get; set; }

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
            Items = GenerateTimeToChoose();

            // Set commands
            CreateCommand = new RelayCommand(async () => await Create());
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
                SubjectDataModel data = new SubjectDataModel();

                data.Major = this.Major.EditText;
                data.Id = this.ID.EditText;
                data.Subject = this.Subject.EditText;
                data.Teacher = this.Teacher.EditText;
                data.Place = this.Class.EditText;
                data.TimeStart = this.DateStart + " " + this.TimeStart;
                data.TimeFinish = this.DateFinish + " " + this.TimeFinish;
                data.Status = 0;

                DateTime dateStart, dateEnd;

                // Kiểm tra xem ngày và thời gian bắt đầu có hợp lệ không
                //try
                //{
                //    dateStart = DateTime.ParseExact(data.TimeStart, "MM/dd/yyyy hh:mm", CultureInfo.CurrentCulture);
                //}
                //catch
                //{
                //    MessageBox.Show("Ngày bắt đầu hoặc thời gian bắt đầu không hợp lệ.\n Mời bạn kiểm tra lại!", "Thông báo",
                //        MessageBoxButton.OK, MessageBoxImage.Error);
                //    return;
                //}

                //// Đến đây, ngày và thời gian bắt đầu đã hợp lệ.
                //// Tiếp tục kiểm tra xem ngày và thời gian kết thúc có hợp lệ không
                //try
                //{
                //    dateEnd = DateTime.ParseExact(data.TimeFinish, "MM/dd/yyyy hh:mm", CultureInfo.CurrentCulture);
                //}
                //catch
                //{
                //    MessageBox.Show("Ngày kết thúc hoặc thời gian kết thúc không hợp lệ.\n Mời bạn kiểm tra lại!", "Thông báo",
                //        MessageBoxButton.OK, MessageBoxImage.Error);
                //    return;
                //}

                //// Đến đây, ngày và thời gian kết thúc đã hợp lệ
                //// Tiếp tục kiểm tra xem ngày và thời gian bắt đầu có trước ngày và thời gian kết thúc không.
                //// Nếu không thì báo lỗi.
                //if (dateStart >= dateEnd)
                //{
                //    MessageBox.Show("Ngày và thời gian bắt đầu phải trước ngày và thời gian kết thúc.\n Mời bạn kiểm tra lại!", "Thông báo",
                //       MessageBoxButton.OK, MessageBoxImage.Error);

                //    return;
                //}

                //// Đến đây ngày và thời gian đã hợp lệ là kiểu datetime 
                //// Tiếp tục kiểm tra xem ngày và thời gian bắt đầu có sau ngày và thời gian hiện tại không.
                //// Nếu không thì báo lỗi.
                //if (DateTimeOffset.UtcNow.Date > dateStart)
                //{
                //    MessageBox.Show("Ngày và thời gian bắt đầu phải sau ngày và thời gian hiện tại.\n Mời bạn kiểm tra lại!", "Thông báo",
                //      MessageBoxButton.OK, MessageBoxImage.Error);

                //    return;
                //}

                // Đến đây, mọi thứ đều đã hợp lệ.
                // Tiến hành gửi data cho server lưu xuống database
                HttpResult result;

                try
                {
                    result = await WebRequest<SubjectDataModel>.PostAsync("http://localhost:51197/api/subject", data);
                }
                catch (Exception ex)
                {
                    MessageBox.Show(ex.Message, "Thông báo", MessageBoxButton.OK, MessageBoxImage.Error);
                    return;
                }

                if (200 != result.StatusCode)
                {
                    MessageBox.Show(result.MessageResponse, "Thông báo", MessageBoxButton.OK, MessageBoxImage.Error);

                    return;
                }

                MessageBox.Show("Tạo môn học thành công", "Thông báo", MessageBoxButton.OK);
            });
        }

        #endregion

        #region Function Helpers

        public ObservableCollection<string> GenerateTimeToChoose()
        {
            ObservableCollection<string> items = new ObservableCollection<string>();
            int minutes =0;
            string hour;
            string minute;

            for (int i = 0; i < 48; ++i)
            {
                hour = (minutes / 60).ToString().Length < 2 ? $"0{minutes / 60}" : $"{minutes / 60 }";
                minute = (minutes % 60).ToString().Length < 2 ? $"0{minutes % 60}" : $"{minutes % 60 }";

                items.Add($"{hour}:{minute}");
                minutes += 30;
            }

            return items;
        }


        #endregion
    }
}
