using System;
using System.Collections.Generic;
using System.Text;

namespace DbModel
{
    /// <summary>
    /// Thông tin tóm gọn bao gồm: Tên môn học, đã được đăng ký chưa, trạng thái như thế nào
    /// </summary>
    public class SubjectDTO
    {
        /// <summary>
        /// Id môn học
        /// </summary>
        public string Id { get; set; }

        /// <summary>
        /// Tên môn học
        /// </summary>
        public string Subject { get; set; }

        /// <summary>
        /// Tình trạng đăng ký
        /// </summary>
        public bool isRegistered { get; set; }

        public SubjectDTO() { }
    }
}
