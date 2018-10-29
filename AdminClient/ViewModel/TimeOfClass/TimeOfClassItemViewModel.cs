using System.Collections.ObjectModel;

namespace AdminClient
{
    /// <summary>
    /// View model cho <see cref="TimeOfClassItemControl"/>
    /// </summary>
    public class TimeOfClassItemViewModel: BaseViewModel
    {
        #region Public Properties
        /// <summary>
        /// List giờ để chọn
        /// </summary>
        public ObservableCollection<string> Items { get; set; }

        /// <summary>
        /// Có chọn ngày này trong tuần không
        /// </summary>
        public bool IsChecked { get; set; }

        /// <summary>
        /// Ngày trong tuần
        /// </summary>
        public string DayInTheWeek { get; set; }

        /// <summary>
        /// Tiết
        /// </summary>
        public string Period { get; set; }

        /// <summary>
        /// Thời gian bắt đầu
        /// </summary>
        public string TimeStart{ get; set; }

        /// <summary>
        /// Thời gian kết thúc
        /// </summary>
        public string TimeFinish { get; set; }


        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public TimeOfClassItemViewModel()
        {
            Items = FunctionHelpersForList.GenerateTimeToChoose();
        }

        #endregion
    }
}
