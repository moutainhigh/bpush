// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: pb_message.proto

package com.beyond.bpush.protobuf;

/**
 * Protobuf type {@code message.PBAPNSMessage}
 */
public final class PBAPNSMessage extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:message.PBAPNSMessage)
    PBAPNSMessageOrBuilder {
  // Use PBAPNSMessage.newBuilder() to construct.
  private PBAPNSMessage(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
    this.unknownFields = builder.getUnknownFields();
  }
  private PBAPNSMessage(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

  private static final PBAPNSMessage defaultInstance;
  public static PBAPNSMessage getDefaultInstance() {
    return defaultInstance;
  }

  public PBAPNSMessage getDefaultInstanceForType() {
    return defaultInstance;
  }

  private final com.google.protobuf.UnknownFieldSet unknownFields;
  @Override
  public final com.google.protobuf.UnknownFieldSet
      getUnknownFields() {
    return this.unknownFields;
  }
  private PBAPNSMessage(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    initFields();
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!parseUnknownField(input, unknownFields,
                                   extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 8: {
            bitField0_ |= 0x00000001;
            offlineMode_ = input.readInt32();
            break;
          }
          case 18: {
            com.beyond.bpush.protobuf.PBAPNSBody.Builder subBuilder = null;
            if (((bitField0_ & 0x00000002) == 0x00000002)) {
              subBuilder = aps_.toBuilder();
            }
            aps_ = input.readMessage(com.beyond.bpush.protobuf.PBAPNSBody.PARSER, extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(aps_);
              aps_ = subBuilder.buildPartial();
            }
            bitField0_ |= 0x00000002;
            break;
          }
          case 26: {
            if (!((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
              userInfo_ = new java.util.ArrayList<com.beyond.bpush.protobuf.PBAPNSUserInfo>();
              mutable_bitField0_ |= 0x00000004;
            }
            userInfo_.add(input.readMessage(com.beyond.bpush.protobuf.PBAPNSUserInfo.PARSER, extensionRegistry));
            break;
          }
          case 32: {
            bitField0_ |= 0x00000004;
            apnsMode_ = input.readInt32();
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e.getMessage()).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
        userInfo_ = java.util.Collections.unmodifiableList(userInfo_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.beyond.bpush.protobuf.PbMessage.internal_static_message_PBAPNSMessage_descriptor;
  }

  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.beyond.bpush.protobuf.PbMessage.internal_static_message_PBAPNSMessage_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.beyond.bpush.protobuf.PBAPNSMessage.class, com.beyond.bpush.protobuf.PBAPNSMessage.Builder.class);
  }

  public static com.google.protobuf.Parser<PBAPNSMessage> PARSER =
      new com.google.protobuf.AbstractParser<PBAPNSMessage>() {
    public PBAPNSMessage parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new PBAPNSMessage(input, extensionRegistry);
    }
  };

  @Override
  public com.google.protobuf.Parser<PBAPNSMessage> getParserForType() {
    return PARSER;
  }

  /**
   * Protobuf enum {@code message.PBAPNSMessage.OfflineModes}
   */
  public enum OfflineModes
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>Ignore = 0;</code>
     */
    Ignore(0, 0),
    /**
     * <code>APNS = 1;</code>
     */
    APNS(1, 1),
    /**
     * <code>SendAfterOnline = 2;</code>
     */
    SendAfterOnline(2, 2),
    ;

    /**
     * <code>Ignore = 0;</code>
     */
    public static final int Ignore_VALUE = 0;
    /**
     * <code>APNS = 1;</code>
     */
    public static final int APNS_VALUE = 1;
    /**
     * <code>SendAfterOnline = 2;</code>
     */
    public static final int SendAfterOnline_VALUE = 2;


    public final int getNumber() { return value; }

    public static OfflineModes valueOf(int value) {
      switch (value) {
        case 0: return Ignore;
        case 1: return APNS;
        case 2: return SendAfterOnline;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<OfflineModes>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<OfflineModes>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<OfflineModes>() {
            public OfflineModes findValueByNumber(int number) {
              return OfflineModes.valueOf(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(index);
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return com.beyond.bpush.protobuf.PBAPNSMessage.getDescriptor().getEnumTypes().get(0);
    }

    private static final OfflineModes[] VALUES = values();

    public static OfflineModes valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }

    private final int index;
    private final int value;

    private OfflineModes(int index, int value) {
      this.index = index;
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:message.PBAPNSMessage.OfflineModes)
  }

  /**
   * Protobuf enum {@code message.PBAPNSMessage.APNSModes}
   */
  public enum APNSModes
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>All = 0;</code>
     */
    All(0, 0),
    /**
     * <code>Signined = 1;</code>
     */
    Signined(1, 1),
    ;

    /**
     * <code>All = 0;</code>
     */
    public static final int All_VALUE = 0;
    /**
     * <code>Signined = 1;</code>
     */
    public static final int Signined_VALUE = 1;


    public final int getNumber() { return value; }

    public static APNSModes valueOf(int value) {
      switch (value) {
        case 0: return All;
        case 1: return Signined;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<APNSModes>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<APNSModes>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<APNSModes>() {
            public APNSModes findValueByNumber(int number) {
              return APNSModes.valueOf(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(index);
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return com.beyond.bpush.protobuf.PBAPNSMessage.getDescriptor().getEnumTypes().get(1);
    }

    private static final APNSModes[] VALUES = values();

    public static APNSModes valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }

    private final int index;
    private final int value;

    private APNSModes(int index, int value) {
      this.index = index;
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:message.PBAPNSMessage.APNSModes)
  }

  private int bitField0_;
  public static final int OFFLINEMODE_FIELD_NUMBER = 1;
  private int offlineMode_;
  /**
   * <code>optional int32 offlineMode = 1;</code>
   */
  public boolean hasOfflineMode() {
    return ((bitField0_ & 0x00000001) == 0x00000001);
  }
  /**
   * <code>optional int32 offlineMode = 1;</code>
   */
  public int getOfflineMode() {
    return offlineMode_;
  }

  public static final int APS_FIELD_NUMBER = 2;
  private com.beyond.bpush.protobuf.PBAPNSBody aps_;
  /**
   * <code>required .message.PBAPNSBody aps = 2;</code>
   */
  public boolean hasAps() {
    return ((bitField0_ & 0x00000002) == 0x00000002);
  }
  /**
   * <code>required .message.PBAPNSBody aps = 2;</code>
   */
  public com.beyond.bpush.protobuf.PBAPNSBody getAps() {
    return aps_;
  }
  /**
   * <code>required .message.PBAPNSBody aps = 2;</code>
   */
  public com.beyond.bpush.protobuf.PBAPNSBodyOrBuilder getApsOrBuilder() {
    return aps_;
  }

  public static final int USERINFO_FIELD_NUMBER = 3;
  private java.util.List<com.beyond.bpush.protobuf.PBAPNSUserInfo> userInfo_;
  /**
   * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
   */
  public java.util.List<com.beyond.bpush.protobuf.PBAPNSUserInfo> getUserInfoList() {
    return userInfo_;
  }
  /**
   * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
   */
  public java.util.List<? extends com.beyond.bpush.protobuf.PBAPNSUserInfoOrBuilder>
      getUserInfoOrBuilderList() {
    return userInfo_;
  }
  /**
   * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
   */
  public int getUserInfoCount() {
    return userInfo_.size();
  }
  /**
   * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
   */
  public com.beyond.bpush.protobuf.PBAPNSUserInfo getUserInfo(int index) {
    return userInfo_.get(index);
  }
  /**
   * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
   */
  public com.beyond.bpush.protobuf.PBAPNSUserInfoOrBuilder getUserInfoOrBuilder(
      int index) {
    return userInfo_.get(index);
  }

  public static final int APNSMODE_FIELD_NUMBER = 4;
  private int apnsMode_;
  /**
   * <code>optional int32 apnsMode = 4;</code>
   */
  public boolean hasApnsMode() {
    return ((bitField0_ & 0x00000004) == 0x00000004);
  }
  /**
   * <code>optional int32 apnsMode = 4;</code>
   */
  public int getApnsMode() {
    return apnsMode_;
  }

  private void initFields() {
    offlineMode_ = 0;
    aps_ = com.beyond.bpush.protobuf.PBAPNSBody.getDefaultInstance();
    userInfo_ = java.util.Collections.emptyList();
    apnsMode_ = 0;
  }
  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    if (!hasAps()) {
      memoizedIsInitialized = 0;
      return false;
    }
    for (int i = 0; i < getUserInfoCount(); i++) {
      if (!getUserInfo(i).isInitialized()) {
        memoizedIsInitialized = 0;
        return false;
      }
    }
    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    getSerializedSize();
    if (((bitField0_ & 0x00000001) == 0x00000001)) {
      output.writeInt32(1, offlineMode_);
    }
    if (((bitField0_ & 0x00000002) == 0x00000002)) {
      output.writeMessage(2, aps_);
    }
    for (int i = 0; i < userInfo_.size(); i++) {
      output.writeMessage(3, userInfo_.get(i));
    }
    if (((bitField0_ & 0x00000004) == 0x00000004)) {
      output.writeInt32(4, apnsMode_);
    }
    getUnknownFields().writeTo(output);
  }

  private int memoizedSerializedSize = -1;
  public int getSerializedSize() {
    int size = memoizedSerializedSize;
    if (size != -1) return size;

    size = 0;
    if (((bitField0_ & 0x00000001) == 0x00000001)) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, offlineMode_);
    }
    if (((bitField0_ & 0x00000002) == 0x00000002)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, aps_);
    }
    for (int i = 0; i < userInfo_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, userInfo_.get(i));
    }
    if (((bitField0_ & 0x00000004) == 0x00000004)) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(4, apnsMode_);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSerializedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  @Override
  protected Object writeReplace()
      throws java.io.ObjectStreamException {
    return super.writeReplace();
  }

  public static com.beyond.bpush.protobuf.PBAPNSMessage parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.beyond.bpush.protobuf.PBAPNSMessage parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.beyond.bpush.protobuf.PBAPNSMessage parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.beyond.bpush.protobuf.PBAPNSMessage parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.beyond.bpush.protobuf.PBAPNSMessage parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static com.beyond.bpush.protobuf.PBAPNSMessage parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }
  public static com.beyond.bpush.protobuf.PBAPNSMessage parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input);
  }
  public static com.beyond.bpush.protobuf.PBAPNSMessage parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input, extensionRegistry);
  }
  public static com.beyond.bpush.protobuf.PBAPNSMessage parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static com.beyond.bpush.protobuf.PBAPNSMessage parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }

  public static Builder newBuilder() { return Builder.create(); }
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder(com.beyond.bpush.protobuf.PBAPNSMessage prototype) {
    return newBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() { return newBuilder(this); }

  @Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessage.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code message.PBAPNSMessage}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:message.PBAPNSMessage)
      com.beyond.bpush.protobuf.PBAPNSMessageOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.beyond.bpush.protobuf.PbMessage.internal_static_message_PBAPNSMessage_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.beyond.bpush.protobuf.PbMessage.internal_static_message_PBAPNSMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.beyond.bpush.protobuf.PBAPNSMessage.class, com.beyond.bpush.protobuf.PBAPNSMessage.Builder.class);
    }

    // Construct using com.beyond.bpush.protobuf.PBAPNSMessage.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        getApsFieldBuilder();
        getUserInfoFieldBuilder();
      }
    }
    private static Builder create() {
      return new Builder();
    }

    public Builder clear() {
      super.clear();
      offlineMode_ = 0;
      bitField0_ = (bitField0_ & ~0x00000001);
      if (apsBuilder_ == null) {
        aps_ = com.beyond.bpush.protobuf.PBAPNSBody.getDefaultInstance();
      } else {
        apsBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000002);
      if (userInfoBuilder_ == null) {
        userInfo_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000004);
      } else {
        userInfoBuilder_.clear();
      }
      apnsMode_ = 0;
      bitField0_ = (bitField0_ & ~0x00000008);
      return this;
    }

    public Builder clone() {
      return create().mergeFrom(buildPartial());
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.beyond.bpush.protobuf.PbMessage.internal_static_message_PBAPNSMessage_descriptor;
    }

    public com.beyond.bpush.protobuf.PBAPNSMessage getDefaultInstanceForType() {
      return com.beyond.bpush.protobuf.PBAPNSMessage.getDefaultInstance();
    }

    public com.beyond.bpush.protobuf.PBAPNSMessage build() {
      com.beyond.bpush.protobuf.PBAPNSMessage result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public com.beyond.bpush.protobuf.PBAPNSMessage buildPartial() {
      com.beyond.bpush.protobuf.PBAPNSMessage result = new com.beyond.bpush.protobuf.PBAPNSMessage(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
        to_bitField0_ |= 0x00000001;
      }
      result.offlineMode_ = offlineMode_;
      if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
        to_bitField0_ |= 0x00000002;
      }
      if (apsBuilder_ == null) {
        result.aps_ = aps_;
      } else {
        result.aps_ = apsBuilder_.build();
      }
      if (userInfoBuilder_ == null) {
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          userInfo_ = java.util.Collections.unmodifiableList(userInfo_);
          bitField0_ = (bitField0_ & ~0x00000004);
        }
        result.userInfo_ = userInfo_;
      } else {
        result.userInfo_ = userInfoBuilder_.build();
      }
      if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
        to_bitField0_ |= 0x00000004;
      }
      result.apnsMode_ = apnsMode_;
      result.bitField0_ = to_bitField0_;
      onBuilt();
      return result;
    }

    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.beyond.bpush.protobuf.PBAPNSMessage) {
        return mergeFrom((com.beyond.bpush.protobuf.PBAPNSMessage)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.beyond.bpush.protobuf.PBAPNSMessage other) {
      if (other == com.beyond.bpush.protobuf.PBAPNSMessage.getDefaultInstance()) return this;
      if (other.hasOfflineMode()) {
        setOfflineMode(other.getOfflineMode());
      }
      if (other.hasAps()) {
        mergeAps(other.getAps());
      }
      if (userInfoBuilder_ == null) {
        if (!other.userInfo_.isEmpty()) {
          if (userInfo_.isEmpty()) {
            userInfo_ = other.userInfo_;
            bitField0_ = (bitField0_ & ~0x00000004);
          } else {
            ensureUserInfoIsMutable();
            userInfo_.addAll(other.userInfo_);
          }
          onChanged();
        }
      } else {
        if (!other.userInfo_.isEmpty()) {
          if (userInfoBuilder_.isEmpty()) {
            userInfoBuilder_.dispose();
            userInfoBuilder_ = null;
            userInfo_ = other.userInfo_;
            bitField0_ = (bitField0_ & ~0x00000004);
            userInfoBuilder_ =
              com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                 getUserInfoFieldBuilder() : null;
          } else {
            userInfoBuilder_.addAllMessages(other.userInfo_);
          }
        }
      }
      if (other.hasApnsMode()) {
        setApnsMode(other.getApnsMode());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      return this;
    }

    public final boolean isInitialized() {
      if (!hasAps()) {

        return false;
      }
      for (int i = 0; i < getUserInfoCount(); i++) {
        if (!getUserInfo(i).isInitialized()) {

          return false;
        }
      }
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.beyond.bpush.protobuf.PBAPNSMessage parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.beyond.bpush.protobuf.PBAPNSMessage) e.getUnfinishedMessage();
        throw e;
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private int offlineMode_ ;
    /**
     * <code>optional int32 offlineMode = 1;</code>
     */
    public boolean hasOfflineMode() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional int32 offlineMode = 1;</code>
     */
    public int getOfflineMode() {
      return offlineMode_;
    }
    /**
     * <code>optional int32 offlineMode = 1;</code>
     */
    public Builder setOfflineMode(int value) {
      bitField0_ |= 0x00000001;
      offlineMode_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 offlineMode = 1;</code>
     */
    public Builder clearOfflineMode() {
      bitField0_ = (bitField0_ & ~0x00000001);
      offlineMode_ = 0;
      onChanged();
      return this;
    }

    private com.beyond.bpush.protobuf.PBAPNSBody aps_ = com.beyond.bpush.protobuf.PBAPNSBody.getDefaultInstance();
    private com.google.protobuf.SingleFieldBuilder<
        com.beyond.bpush.protobuf.PBAPNSBody, com.beyond.bpush.protobuf.PBAPNSBody.Builder, com.beyond.bpush.protobuf.PBAPNSBodyOrBuilder> apsBuilder_;
    /**
     * <code>required .message.PBAPNSBody aps = 2;</code>
     */
    public boolean hasAps() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>required .message.PBAPNSBody aps = 2;</code>
     */
    public com.beyond.bpush.protobuf.PBAPNSBody getAps() {
      if (apsBuilder_ == null) {
        return aps_;
      } else {
        return apsBuilder_.getMessage();
      }
    }
    /**
     * <code>required .message.PBAPNSBody aps = 2;</code>
     */
    public Builder setAps(com.beyond.bpush.protobuf.PBAPNSBody value) {
      if (apsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        aps_ = value;
        onChanged();
      } else {
        apsBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000002;
      return this;
    }
    /**
     * <code>required .message.PBAPNSBody aps = 2;</code>
     */
    public Builder setAps(
        com.beyond.bpush.protobuf.PBAPNSBody.Builder builderForValue) {
      if (apsBuilder_ == null) {
        aps_ = builderForValue.build();
        onChanged();
      } else {
        apsBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000002;
      return this;
    }
    /**
     * <code>required .message.PBAPNSBody aps = 2;</code>
     */
    public Builder mergeAps(com.beyond.bpush.protobuf.PBAPNSBody value) {
      if (apsBuilder_ == null) {
        if (((bitField0_ & 0x00000002) == 0x00000002) &&
            aps_ != com.beyond.bpush.protobuf.PBAPNSBody.getDefaultInstance()) {
          aps_ =
            com.beyond.bpush.protobuf.PBAPNSBody.newBuilder(aps_).mergeFrom(value).buildPartial();
        } else {
          aps_ = value;
        }
        onChanged();
      } else {
        apsBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000002;
      return this;
    }
    /**
     * <code>required .message.PBAPNSBody aps = 2;</code>
     */
    public Builder clearAps() {
      if (apsBuilder_ == null) {
        aps_ = com.beyond.bpush.protobuf.PBAPNSBody.getDefaultInstance();
        onChanged();
      } else {
        apsBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000002);
      return this;
    }
    /**
     * <code>required .message.PBAPNSBody aps = 2;</code>
     */
    public com.beyond.bpush.protobuf.PBAPNSBody.Builder getApsBuilder() {
      bitField0_ |= 0x00000002;
      onChanged();
      return getApsFieldBuilder().getBuilder();
    }
    /**
     * <code>required .message.PBAPNSBody aps = 2;</code>
     */
    public com.beyond.bpush.protobuf.PBAPNSBodyOrBuilder getApsOrBuilder() {
      if (apsBuilder_ != null) {
        return apsBuilder_.getMessageOrBuilder();
      } else {
        return aps_;
      }
    }
    /**
     * <code>required .message.PBAPNSBody aps = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilder<
        com.beyond.bpush.protobuf.PBAPNSBody, com.beyond.bpush.protobuf.PBAPNSBody.Builder, com.beyond.bpush.protobuf.PBAPNSBodyOrBuilder>
        getApsFieldBuilder() {
      if (apsBuilder_ == null) {
        apsBuilder_ = new com.google.protobuf.SingleFieldBuilder<
            com.beyond.bpush.protobuf.PBAPNSBody, com.beyond.bpush.protobuf.PBAPNSBody.Builder, com.beyond.bpush.protobuf.PBAPNSBodyOrBuilder>(
                getAps(),
                getParentForChildren(),
                isClean());
        aps_ = null;
      }
      return apsBuilder_;
    }

    private java.util.List<com.beyond.bpush.protobuf.PBAPNSUserInfo> userInfo_ =
      java.util.Collections.emptyList();
    private void ensureUserInfoIsMutable() {
      if (!((bitField0_ & 0x00000004) == 0x00000004)) {
        userInfo_ = new java.util.ArrayList<com.beyond.bpush.protobuf.PBAPNSUserInfo>(userInfo_);
        bitField0_ |= 0x00000004;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilder<
        com.beyond.bpush.protobuf.PBAPNSUserInfo, com.beyond.bpush.protobuf.PBAPNSUserInfo.Builder, com.beyond.bpush.protobuf.PBAPNSUserInfoOrBuilder> userInfoBuilder_;

    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public java.util.List<com.beyond.bpush.protobuf.PBAPNSUserInfo> getUserInfoList() {
      if (userInfoBuilder_ == null) {
        return java.util.Collections.unmodifiableList(userInfo_);
      } else {
        return userInfoBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public int getUserInfoCount() {
      if (userInfoBuilder_ == null) {
        return userInfo_.size();
      } else {
        return userInfoBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public com.beyond.bpush.protobuf.PBAPNSUserInfo getUserInfo(int index) {
      if (userInfoBuilder_ == null) {
        return userInfo_.get(index);
      } else {
        return userInfoBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public Builder setUserInfo(
        int index, com.beyond.bpush.protobuf.PBAPNSUserInfo value) {
      if (userInfoBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureUserInfoIsMutable();
        userInfo_.set(index, value);
        onChanged();
      } else {
        userInfoBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public Builder setUserInfo(
        int index, com.beyond.bpush.protobuf.PBAPNSUserInfo.Builder builderForValue) {
      if (userInfoBuilder_ == null) {
        ensureUserInfoIsMutable();
        userInfo_.set(index, builderForValue.build());
        onChanged();
      } else {
        userInfoBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public Builder addUserInfo(com.beyond.bpush.protobuf.PBAPNSUserInfo value) {
      if (userInfoBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureUserInfoIsMutable();
        userInfo_.add(value);
        onChanged();
      } else {
        userInfoBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public Builder addUserInfo(
        int index, com.beyond.bpush.protobuf.PBAPNSUserInfo value) {
      if (userInfoBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureUserInfoIsMutable();
        userInfo_.add(index, value);
        onChanged();
      } else {
        userInfoBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public Builder addUserInfo(
        com.beyond.bpush.protobuf.PBAPNSUserInfo.Builder builderForValue) {
      if (userInfoBuilder_ == null) {
        ensureUserInfoIsMutable();
        userInfo_.add(builderForValue.build());
        onChanged();
      } else {
        userInfoBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public Builder addUserInfo(
        int index, com.beyond.bpush.protobuf.PBAPNSUserInfo.Builder builderForValue) {
      if (userInfoBuilder_ == null) {
        ensureUserInfoIsMutable();
        userInfo_.add(index, builderForValue.build());
        onChanged();
      } else {
        userInfoBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public Builder addAllUserInfo(
        Iterable<? extends com.beyond.bpush.protobuf.PBAPNSUserInfo> values) {
      if (userInfoBuilder_ == null) {
        ensureUserInfoIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, userInfo_);
        onChanged();
      } else {
        userInfoBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public Builder clearUserInfo() {
      if (userInfoBuilder_ == null) {
        userInfo_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000004);
        onChanged();
      } else {
        userInfoBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public Builder removeUserInfo(int index) {
      if (userInfoBuilder_ == null) {
        ensureUserInfoIsMutable();
        userInfo_.remove(index);
        onChanged();
      } else {
        userInfoBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public com.beyond.bpush.protobuf.PBAPNSUserInfo.Builder getUserInfoBuilder(
        int index) {
      return getUserInfoFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public com.beyond.bpush.protobuf.PBAPNSUserInfoOrBuilder getUserInfoOrBuilder(
        int index) {
      if (userInfoBuilder_ == null) {
        return userInfo_.get(index);  } else {
        return userInfoBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public java.util.List<? extends com.beyond.bpush.protobuf.PBAPNSUserInfoOrBuilder> 
         getUserInfoOrBuilderList() {
      if (userInfoBuilder_ != null) {
        return userInfoBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(userInfo_);
      }
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public com.beyond.bpush.protobuf.PBAPNSUserInfo.Builder addUserInfoBuilder() {
      return getUserInfoFieldBuilder().addBuilder(
          com.beyond.bpush.protobuf.PBAPNSUserInfo.getDefaultInstance());
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public com.beyond.bpush.protobuf.PBAPNSUserInfo.Builder addUserInfoBuilder(
        int index) {
      return getUserInfoFieldBuilder().addBuilder(
          index, com.beyond.bpush.protobuf.PBAPNSUserInfo.getDefaultInstance());
    }
    /**
     * <code>repeated .message.PBAPNSUserInfo userInfo = 3;</code>
     */
    public java.util.List<com.beyond.bpush.protobuf.PBAPNSUserInfo.Builder> 
         getUserInfoBuilderList() {
      return getUserInfoFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilder<
        com.beyond.bpush.protobuf.PBAPNSUserInfo, com.beyond.bpush.protobuf.PBAPNSUserInfo.Builder, com.beyond.bpush.protobuf.PBAPNSUserInfoOrBuilder> 
        getUserInfoFieldBuilder() {
      if (userInfoBuilder_ == null) {
        userInfoBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
            com.beyond.bpush.protobuf.PBAPNSUserInfo, com.beyond.bpush.protobuf.PBAPNSUserInfo.Builder, com.beyond.bpush.protobuf.PBAPNSUserInfoOrBuilder>(
                userInfo_,
                ((bitField0_ & 0x00000004) == 0x00000004),
                getParentForChildren(),
                isClean());
        userInfo_ = null;
      }
      return userInfoBuilder_;
    }

    private int apnsMode_ ;
    /**
     * <code>optional int32 apnsMode = 4;</code>
     */
    public boolean hasApnsMode() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional int32 apnsMode = 4;</code>
     */
    public int getApnsMode() {
      return apnsMode_;
    }
    /**
     * <code>optional int32 apnsMode = 4;</code>
     */
    public Builder setApnsMode(int value) {
      bitField0_ |= 0x00000008;
      apnsMode_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 apnsMode = 4;</code>
     */
    public Builder clearApnsMode() {
      bitField0_ = (bitField0_ & ~0x00000008);
      apnsMode_ = 0;
      onChanged();
      return this;
    }

    // @@protoc_insertion_point(builder_scope:message.PBAPNSMessage)
  }

  static {
    defaultInstance = new PBAPNSMessage(true);
    defaultInstance.initFields();
  }

  // @@protoc_insertion_point(class_scope:message.PBAPNSMessage)
}

