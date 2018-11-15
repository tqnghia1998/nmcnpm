using DbModel;
using System.Collections.Generic;

namespace DbModel
{
    /// <summary>
    /// Các thông tin cần thiết để sử dụng chức năng create subject trên server
    /// </summary>
    public class CreateSubjectCredentialsDataModel
    {
        private SubjectDataModel subjectDataModel;
        private ScheduleDataModel scheduleDataModel;

        public CreateSubjectCredentialsDataModel()
        {
        }

        public CreateSubjectCredentialsDataModel(SubjectDataModel subjectDataModel, ScheduleDataModel scheduleDataModel)
        {
            this.subjectDataModel = subjectDataModel;
            this.scheduleDataModel = scheduleDataModel;
        }

        /// <summary>
        /// Thông tin môn học
        /// </summary>
        public SubjectDataModel Subject { get; set; }

        /// <summary>
        /// Thông tin thời khóa biểu của môn học
        /// </summary>
        public List<ScheduleDataModel> Schedule { get; set; }
    }
}
