package cn.edu.xmu.oomall.wechatpay.mapper;

import cn.edu.xmu.oomall.wechatpay.model.po.WeChatPayReceiverPo;
import cn.edu.xmu.oomall.wechatpay.model.po.WeChatPayReceiverPoExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface WeChatPayReceiverPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechatpay_receiver
     *
     * @mbg.generated
     */
    @Delete({
        "delete from wechatpay_receiver",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechatpay_receiver
     *
     * @mbg.generated
     */
    @Insert({
        "insert into wechatpay_receiver (`appid`, `type`, ",
        "`account`, `name`, `relation_type`)",
        "values (#{appid,jdbcType=VARCHAR}, #{type,jdbcType=VARCHAR}, ",
        "#{account,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, #{relationType,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(WeChatPayReceiverPo row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechatpay_receiver
     *
     * @mbg.generated
     */
    @InsertProvider(type=WeChatPayReceiverPoSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(WeChatPayReceiverPo row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechatpay_receiver
     *
     * @mbg.generated
     */
    @SelectProvider(type=WeChatPayReceiverPoSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="appid", property="appid", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR),
        @Result(column="account", property="account", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="relation_type", property="relationType", jdbcType=JdbcType.VARCHAR)
    })
    List<WeChatPayReceiverPo> selectByExample(WeChatPayReceiverPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechatpay_receiver
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "`id`, `appid`, `type`, `account`, `name`, `relation_type`",
        "from wechatpay_receiver",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="appid", property="appid", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR),
        @Result(column="account", property="account", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="relation_type", property="relationType", jdbcType=JdbcType.VARCHAR)
    })
    WeChatPayReceiverPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechatpay_receiver
     *
     * @mbg.generated
     */
    @UpdateProvider(type=WeChatPayReceiverPoSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("row") WeChatPayReceiverPo row, @Param("example") WeChatPayReceiverPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechatpay_receiver
     *
     * @mbg.generated
     */
    @UpdateProvider(type=WeChatPayReceiverPoSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("row") WeChatPayReceiverPo row, @Param("example") WeChatPayReceiverPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechatpay_receiver
     *
     * @mbg.generated
     */
    @UpdateProvider(type=WeChatPayReceiverPoSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(WeChatPayReceiverPo row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechatpay_receiver
     *
     * @mbg.generated
     */
    @Update({
        "update wechatpay_receiver",
        "set `appid` = #{appid,jdbcType=VARCHAR},",
          "`type` = #{type,jdbcType=VARCHAR},",
          "`account` = #{account,jdbcType=VARCHAR},",
          "`name` = #{name,jdbcType=VARCHAR},",
          "`relation_type` = #{relationType,jdbcType=VARCHAR}",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(WeChatPayReceiverPo row);
}