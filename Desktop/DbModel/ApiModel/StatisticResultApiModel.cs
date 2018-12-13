using System.Collections.Generic;

namespace DbModel
{
    /// <summary>
    /// The result of a successful statistic request via API
    /// </summary>
    public class StatisticResultApiModel
    {
        #region Public Properties

        /// <summary>
        /// Danh sách các môn học
        /// </summary>
        public List<StatisticSubjectItemDTO> ListSubject { get; set; }

        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public StatisticResultApiModel()
        {

        }

        #endregion
    }
}
