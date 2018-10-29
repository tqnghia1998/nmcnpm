using System.Collections.ObjectModel;

namespace AdminClient
{
    /// <summary>
    /// View model cho <see cref="ListTimeOfClassControl"/>
    /// </summary>
    public class ListTimeOfClassViewModel: BaseViewModel
    {
        #region Public Properties

        /// <summary>
        /// Danh sách các view model cho từng item
        /// </summary>
        public ObservableCollection<TimeOfClassItemViewModel> Items { get; set; }

        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public ListTimeOfClassViewModel()
        {

        }

        #endregion
    }
}
