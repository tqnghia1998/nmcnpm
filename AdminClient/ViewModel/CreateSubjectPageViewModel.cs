using DbModel;
using System.Collections.ObjectModel;
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
        /// True nếu ngày bắt đầu không hợp lệ
        /// </summary>
        public bool ErrorDateStart { get; set; }

        /// <summary>
        /// True nếu thời gian bắt đầu không hợp lệ
        /// </summary>
        public bool ErrorTimeStart { get; set; }

        /// <summary>
        /// True nếu ngày kết thúc không hợp lệ
        /// </summary>
        public bool ErrorDateFinish { get; set; }

        /// <summary>
        /// True nếu thời gian kết thúc không hợp lệ
        /// </summary>
        public bool ErrorTimeFinish { get; set; }

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
            CreateCommand = new RelayCommand(Create);
        }

        #endregion


        #region Command Methods

        /// <summary>
        /// Hàm tạo môn học
        /// </summary>
        public void Create()
        {
            SubjectDataModel data = new SubjectDataModel();

            data.Major = this.Major.EditText;
            data.Id = this.ID.EditText;
            data.Subject = this.Subject.EditText;
            data.Teacher = this.Teacher.EditText;
            data.Place = this.Class.EditText;
            data.TimeStart = this.DateStart + " " + this.TimeStart;
            data.TimeFinish = this.DateFinish + " " + this.TimeFinish;
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
