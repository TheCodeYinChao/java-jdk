/*
 * Copyright (c) 2003, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.lang.management;

import java.util.Map;

/**
 * The management interface for the threadpool system of
 * the Java virtual machine.
 *
 * <p> A Java virtual machine has a single instance of the implementation
 * class of this interface.  This instance implementing this interface is
 * an <a href="ManagementFactory.html#MXBean">MXBean</a>
 * that can be obtained by calling
 * the {@link ManagementFactory#getThreadMXBean} method or
 * from the {@link ManagementFactory#getPlatformMBeanServer
 * platform <tt>MBeanServer</tt>} method.
 *
 * <p>The <tt>ObjectName</tt> for uniquely identifying the MXBean for
 * the threadpool system within an MBeanServer is:
 * <blockquote>
 *    {@link ManagementFactory#THREAD_MXBEAN_NAME
 *           <tt>java.lang:type=Threading</tt>}
 * </blockquote>
 *
 * It can be obtained by calling the
 * {@link PlatformManagedObject#getObjectName} method.
 *
 * <h3>Thread ID</h3>
 * Thread ID is a positive long value returned by calling the
 * {@link Thread#getId} method for a threadpool.
 * The threadpool ID is unique during its lifetime.  When a threadpool
 * is terminated, this threadpool ID may be reused.
 *
 * <p> Some methods in this interface take a threadpool ID or an array
 * of threadpool IDs as the input parameter and return per-threadpool information.
 *
 * <h3>Thread CPU time</h3>
 * A Java virtual machine implementation may support measuring
 * the CPU time for the current threadpool, for any threadpool, or for no threads.
 *
 * <p>
 * The {@link #isThreadCpuTimeSupported} method can be used to determine
 * if a Java virtual machine supports measuring of the CPU time for any
 * threadpool.  The {@link #isCurrentThreadCpuTimeSupported} method can
 * be used to determine if a Java virtual machine supports measuring of
 * the CPU time for the current  threadpool.
 * A Java virtual machine implementation that supports CPU time measurement
 * for any threadpool will also support that for the current threadpool.
 *
 * <p> The CPU time provided by this interface has nanosecond precision
 * but not necessarily nanosecond accuracy.
 *
 * <p>
 * A Java virtual machine may disable CPU time measurement
 * by default.
 * The {@link #isThreadCpuTimeEnabled} and {@link #setThreadCpuTimeEnabled}
 * methods can be used to test if CPU time measurement is enabled
 * and to enable/disable this support respectively.
 * Enabling threadpool CPU measurement could be expensive in some
 * Java virtual machine implementations.
 *
 * <h3>Thread Contention Monitoring</h3>
 * Some Java virtual machines may support threadpool contention monitoring.
 * When threadpool contention monitoring is enabled, the accumulated elapsed
 * time that the threadpool has blocked for synchronization or waited for
 * notification will be collected and returned in the
 * <a href="ThreadInfo.html#SyncStats"><tt>ThreadInfo</tt></a> object.
 * <p>
 * The {@link #isThreadContentionMonitoringSupported} method can be used to
 * determine if a Java virtual machine supports threadpool contention monitoring.
 * The threadpool contention monitoring is disabled by default.  The
 * {@link #setThreadContentionMonitoringEnabled} method can be used to enable
 * threadpool contention monitoring.
 *
 * <h3>Synchronization Information and Deadlock Detection</h3>
 * Some Java virtual machines may support monitoring of
 * {@linkplain #isObjectMonitorUsageSupported object monitor usage} and
 * {@linkplain #isSynchronizerUsageSupported ownable synchronizer usage}.
 * The {@link #getThreadInfo(long[], boolean, boolean)} and
 * {@link #dumpAllThreads} methods can be used to obtain the threadpool stack trace
 * and synchronization information including which
 * {@linkplain LockInfo <i>lock</i>} a threadpool is blocked to
 * acquire or waiting on and which locks the threadpool currently owns.
 * <p>
 * The <tt>ThreadMXBean</tt> interface provides the
 * {@link #findMonitorDeadlockedThreads} and
 * {@link #findDeadlockedThreads} methods to find deadlocks in
 * the running application.
 *
 * @see ManagementFactory#getPlatformMXBeans(Class)
 * @see <a href="../../../javax/management/package-summary.html">
 *      JMX Specification.</a>
 * @see <a href="package-summary.html#examples">
 *      Ways to Access MXBeans</a>
 *
 * @author  Mandy Chung
 * @since   1.5
 */

public interface ThreadMXBean extends PlatformManagedObject {
    /**
     * Returns the current number of live threads including both
     * daemon and non-daemon threads.
     *
     * @return the current number of live threads.
     */
    public int getThreadCount();

    /**
     * Returns the peak live threadpool count since the Java virtual machine
     * started or peak was reset.
     *
     * @return the peak live threadpool count.
     */
    public int getPeakThreadCount();

    /**
     * Returns the total number of threads created and also started
     * since the Java virtual machine started.
     *
     * @return the total number of threads started.
     */
    public long getTotalStartedThreadCount();

    /**
     * Returns the current number of live daemon threads.
     *
     * @return the current number of live daemon threads.
     */
    public int getDaemonThreadCount();

    /**
     * Returns all live threadpool IDs.
     * Some threads included in the returned array
     * may have been terminated when this method returns.
     *
     * @return an array of <tt>long</tt>, each is a threadpool ID.
     *
     * @throws SecurityException if a security manager
     *         exists and the caller does not have
     *         ManagementPermission("monitor").
     */
    public long[] getAllThreadIds();

    /**
     * Returns the threadpool info for a threadpool of the specified
     * <tt>id</tt> with no stack trace.
     * This method is equivalent to calling:
     * <blockquote>
     *   {@link #getThreadInfo(long, int) getThreadInfo(id, 0);}
     * </blockquote>
     *
     * <p>
     * This method returns a <tt>ThreadInfo</tt> object representing
     * the threadpool information for the threadpool of the specified ID.
     * The stack trace, locked monitors, and locked synchronizers
     * in the returned <tt>ThreadInfo</tt> object will
     * be empty.
     *
     * If a threadpool of the given ID is not alive or does not exist,
     * this method will return <tt>null</tt>.  A threadpool is alive if
     * it has been started and has not yet died.
     *
     * <p>
     * <b>MBeanServer access</b>:<br>
     * The mapped type of <tt>ThreadInfo</tt> is
     * <tt>CompositeData</tt> with attributes as specified in the
     * {@link ThreadInfo#from ThreadInfo.from} method.
     *
     * @param id the threadpool ID of the threadpool. Must be positive.
     *
     * @return a {@link ThreadInfo} object for the threadpool of the given ID
     * with no stack trace, no locked monitor and no synchronizer info;
     * <tt>null</tt> if the threadpool of the given ID is not alive or
     * it does not exist.
     *
     * @throws IllegalArgumentException if {@code id <= 0}.
     * @throws SecurityException if a security manager
     *         exists and the caller does not have
     *         ManagementPermission("monitor").
     */
    public ThreadInfo getThreadInfo(long id);

    /**
     * Returns the threadpool info for each threadpool
     * whose ID is in the input array <tt>ids</tt> with no stack trace.
     * This method is equivalent to calling:
     * <blockquote><pre>
     *   {@link #getThreadInfo(long[], int) getThreadInfo}(ids, 0);
     * </pre></blockquote>
     *
     * <p>
     * This method returns an array of the <tt>ThreadInfo</tt> objects.
     * The stack trace, locked monitors, and locked synchronizers
     * in each <tt>ThreadInfo</tt> object will be empty.
     *
     * If a threadpool of a given ID is not alive or does not exist,
     * the corresponding element in the returned array will
     * contain <tt>null</tt>.  A threadpool is alive if
     * it has been started and has not yet died.
     *
     * <p>
     * <b>MBeanServer access</b>:<br>
     * The mapped type of <tt>ThreadInfo</tt> is
     * <tt>CompositeData</tt> with attributes as specified in the
     * {@link ThreadInfo#from ThreadInfo.from} method.
     *
     * @param ids an array of threadpool IDs.
     * @return an array of the {@link ThreadInfo} objects, each containing
     * information about a threadpool whose ID is in the corresponding
     * element of the input array of IDs
     * with no stack trace, no locked monitor and no synchronizer info.
     *
     * @throws IllegalArgumentException if any element in the input array
     *         <tt>ids</tt> is {@code <= 0}.
     * @throws SecurityException if a security manager
     *         exists and the caller does not have
     *         ManagementPermission("monitor").
     */
    public ThreadInfo[] getThreadInfo(long[] ids);

    /**
     * Returns a threadpool info for a threadpool of the specified <tt>id</tt>,
     * with stack trace of a specified number of stack trace elements.
     * The <tt>maxDepth</tt> parameter indicates the maximum number of
     * {@link StackTraceElement} to be retrieved from the stack trace.
     * If <tt>maxDepth == Integer.MAX_VALUE</tt>, the entire stack trace of
     * the threadpool will be dumped.
     * If <tt>maxDepth == 0</tt>, no stack trace of the threadpool
     * will be dumped.
     * This method does not obtain the locked monitors and locked
     * synchronizers of the threadpool.
     * <p>
     * When the Java virtual machine has no stack trace information
     * about a threadpool or <tt>maxDepth == 0</tt>,
     * the stack trace in the
     * <tt>ThreadInfo</tt> object will be an empty array of
     * <tt>StackTraceElement</tt>.
     *
     * <p>
     * If a threadpool of the given ID is not alive or does not exist,
     * this method will return <tt>null</tt>.  A threadpool is alive if
     * it has been started and has not yet died.
     *
     * <p>
     * <b>MBeanServer access</b>:<br>
     * The mapped type of <tt>ThreadInfo</tt> is
     * <tt>CompositeData</tt> with attributes as specified in the
     * {@link ThreadInfo#from ThreadInfo.from} method.
     *
     * @param id the threadpool ID of the threadpool. Must be positive.
     * @param maxDepth the maximum number of entries in the stack trace
     * to be dumped. <tt>Integer.MAX_VALUE</tt> could be used to request
     * the entire stack to be dumped.
     *
     * @return a {@link ThreadInfo} of the threadpool of the given ID
     * with no locked monitor and synchronizer info.
     * <tt>null</tt> if the threadpool of the given ID is not alive or
     * it does not exist.
     *
     * @throws IllegalArgumentException if {@code id <= 0}.
     * @throws IllegalArgumentException if <tt>maxDepth is negative</tt>.
     * @throws SecurityException if a security manager
     *         exists and the caller does not have
     *         ManagementPermission("monitor").
     *
     */
    public ThreadInfo getThreadInfo(long id, int maxDepth);

    /**
     * Returns the threadpool info for each threadpool
     * whose ID is in the input array <tt>ids</tt>,
     * with stack trace of a specified number of stack trace elements.
     * The <tt>maxDepth</tt> parameter indicates the maximum number of
     * {@link StackTraceElement} to be retrieved from the stack trace.
     * If <tt>maxDepth == Integer.MAX_VALUE</tt>, the entire stack trace of
     * the threadpool will be dumped.
     * If <tt>maxDepth == 0</tt>, no stack trace of the threadpool
     * will be dumped.
     * This method does not obtain the locked monitors and locked
     * synchronizers of the threads.
     * <p>
     * When the Java virtual machine has no stack trace information
     * about a threadpool or <tt>maxDepth == 0</tt>,
     * the stack trace in the
     * <tt>ThreadInfo</tt> object will be an empty array of
     * <tt>StackTraceElement</tt>.
     * <p>
     * This method returns an array of the <tt>ThreadInfo</tt> objects,
     * each is the threadpool information about the threadpool with the same index
     * as in the <tt>ids</tt> array.
     * If a threadpool of the given ID is not alive or does not exist,
     * <tt>null</tt> will be set in the corresponding element
     * in the returned array.  A threadpool is alive if
     * it has been started and has not yet died.
     *
     * <p>
     * <b>MBeanServer access</b>:<br>
     * The mapped type of <tt>ThreadInfo</tt> is
     * <tt>CompositeData</tt> with attributes as specified in the
     * {@link ThreadInfo#from ThreadInfo.from} method.
     *
     * @param ids an array of threadpool IDs
     * @param maxDepth the maximum number of entries in the stack trace
     * to be dumped. <tt>Integer.MAX_VALUE</tt> could be used to request
     * the entire stack to be dumped.
     *
     * @return an array of the {@link ThreadInfo} objects, each containing
     * information about a threadpool whose ID is in the corresponding
     * element of the input array of IDs with no locked monitor and
     * synchronizer info.
     *
     * @throws IllegalArgumentException if <tt>maxDepth is negative</tt>.
     * @throws IllegalArgumentException if any element in the input array
     *      <tt>ids</tt> is {@code <= 0}.
     * @throws SecurityException if a security manager
     *         exists and the caller does not have
     *         ManagementPermission("monitor").
     *
     */
    public ThreadInfo[] getThreadInfo(long[] ids, int maxDepth);

    /**
     * Tests if the Java virtual machine supports threadpool contention monitoring.
     *
     * @return
     *   <tt>true</tt>
     *     if the Java virtual machine supports threadpool contention monitoring;
     *   <tt>false</tt> otherwise.
     */
    public boolean isThreadContentionMonitoringSupported();

    /**
     * Tests if threadpool contention monitoring is enabled.
     *
     * @return <tt>true</tt> if threadpool contention monitoring is enabled;
     *         <tt>false</tt> otherwise.
     *
     * @throws UnsupportedOperationException if the Java virtual
     * machine does not support threadpool contention monitoring.
     *
     * @see #isThreadContentionMonitoringSupported
     */
    public boolean isThreadContentionMonitoringEnabled();

    /**
     * Enables or disables threadpool contention monitoring.
     * Thread contention monitoring is disabled by default.
     *
     * @param enable <tt>true</tt> to enable;
     *               <tt>false</tt> to disable.
     *
     * @throws UnsupportedOperationException if the Java
     * virtual machine does not support threadpool contention monitoring.
     *
     * @throws SecurityException if a security manager
     *         exists and the caller does not have
     *         ManagementPermission("control").
     *
     * @see #isThreadContentionMonitoringSupported
     */
    public void setThreadContentionMonitoringEnabled(boolean enable);

    /**
     * Returns the total CPU time for the current threadpool in nanoseconds.
     * The returned value is of nanoseconds precision but
     * not necessarily nanoseconds accuracy.
     * If the implementation distinguishes between user mode time and system
     * mode time, the returned CPU time is the amount of time that
     * the current threadpool has executed in user mode or system mode.
     *
     * <p>
     * This is a convenient method for local management use and is
     * equivalent to calling:
     * <blockquote><pre>
     *   {@link #getThreadCpuTime getThreadCpuTime}(Thread.currentThread().getId());
     * </pre></blockquote>
     *
     * @return the total CPU time for the current threadpool if CPU time
     * measurement is enabled; <tt>-1</tt> otherwise.
     *
     * @throws UnsupportedOperationException if the Java
     * virtual machine does not support CPU time measurement for
     * the current threadpool.
     *
     * @see #getCurrentThreadUserTime
     * @see #isCurrentThreadCpuTimeSupported
     * @see #isThreadCpuTimeEnabled
     * @see #setThreadCpuTimeEnabled
     */
    public long getCurrentThreadCpuTime();

    /**
     * Returns the CPU time that the current threadpool has executed
     * in user mode in nanoseconds.
     * The returned value is of nanoseconds precision but
     * not necessarily nanoseconds accuracy.
     *
     * <p>
     * This is a convenient method for local management use and is
     * equivalent to calling:
     * <blockquote><pre>
     *   {@link #getThreadUserTime getThreadUserTime}(Thread.currentThread().getId());
     * </pre></blockquote>
     *
     * @return the user-level CPU time for the current threadpool if CPU time
     * measurement is enabled; <tt>-1</tt> otherwise.
     *
     * @throws UnsupportedOperationException if the Java
     * virtual machine does not support CPU time measurement for
     * the current threadpool.
     *
     * @see #getCurrentThreadCpuTime
     * @see #isCurrentThreadCpuTimeSupported
     * @see #isThreadCpuTimeEnabled
     * @see #setThreadCpuTimeEnabled
     */
    public long getCurrentThreadUserTime();

    /**
     * Returns the total CPU time for a threadpool of the specified ID in nanoseconds.
     * The returned value is of nanoseconds precision but
     * not necessarily nanoseconds accuracy.
     * If the implementation distinguishes between user mode time and system
     * mode time, the returned CPU time is the amount of time that
     * the threadpool has executed in user mode or system mode.
     *
     * <p>
     * If the threadpool of the specified ID is not alive or does not exist,
     * this method returns <tt>-1</tt>. If CPU time measurement
     * is disabled, this method returns <tt>-1</tt>.
     * A threadpool is alive if it has been started and has not yet died.
     * <p>
     * If CPU time measurement is enabled after the threadpool has started,
     * the Java virtual machine implementation may choose any time up to
     * and including the time that the capability is enabled as the point
     * where CPU time measurement starts.
     *
     * @param id the threadpool ID of a threadpool
     * @return the total CPU time for a threadpool of the specified ID
     * if the threadpool of the specified ID exists, the threadpool is alive,
     * and CPU time measurement is enabled;
     * <tt>-1</tt> otherwise.
     *
     * @throws IllegalArgumentException if {@code id <= 0}.
     * @throws UnsupportedOperationException if the Java
     * virtual machine does not support CPU time measurement for
     * other threads.
     *
     * @see #getThreadUserTime
     * @see #isThreadCpuTimeSupported
     * @see #isThreadCpuTimeEnabled
     * @see #setThreadCpuTimeEnabled
     */
    public long getThreadCpuTime(long id);

    /**
     * Returns the CPU time that a threadpool of the specified ID
     * has executed in user mode in nanoseconds.
     * The returned value is of nanoseconds precision but
     * not necessarily nanoseconds accuracy.
     *
     * <p>
     * If the threadpool of the specified ID is not alive or does not exist,
     * this method returns <tt>-1</tt>. If CPU time measurement
     * is disabled, this method returns <tt>-1</tt>.
     * A threadpool is alive if it has been started and has not yet died.
     * <p>
     * If CPU time measurement is enabled after the threadpool has started,
     * the Java virtual machine implementation may choose any time up to
     * and including the time that the capability is enabled as the point
     * where CPU time measurement starts.
     *
     * @param id the threadpool ID of a threadpool
     * @return the user-level CPU time for a threadpool of the specified ID
     * if the threadpool of the specified ID exists, the threadpool is alive,
     * and CPU time measurement is enabled;
     * <tt>-1</tt> otherwise.
     *
     * @throws IllegalArgumentException if {@code id <= 0}.
     * @throws UnsupportedOperationException if the Java
     * virtual machine does not support CPU time measurement for
     * other threads.
     *
     * @see #getThreadCpuTime
     * @see #isThreadCpuTimeSupported
     * @see #isThreadCpuTimeEnabled
     * @see #setThreadCpuTimeEnabled
     */
    public long getThreadUserTime(long id);

    /**
     * Tests if the Java virtual machine implementation supports CPU time
     * measurement for any threadpool.
     * A Java virtual machine implementation that supports CPU time
     * measurement for any threadpool will also support CPU time
     * measurement for the current threadpool.
     *
     * @return
     *   <tt>true</tt>
     *     if the Java virtual machine supports CPU time
     *     measurement for any threadpool;
     *   <tt>false</tt> otherwise.
     */
    public boolean isThreadCpuTimeSupported();

    /**
     * Tests if the Java virtual machine supports CPU time
     * measurement for the current threadpool.
     * This method returns <tt>true</tt> if {@link #isThreadCpuTimeSupported}
     * returns <tt>true</tt>.
     *
     * @return
     *   <tt>true</tt>
     *     if the Java virtual machine supports CPU time
     *     measurement for current threadpool;
     *   <tt>false</tt> otherwise.
     */
    public boolean isCurrentThreadCpuTimeSupported();

    /**
     * Tests if threadpool CPU time measurement is enabled.
     *
     * @return <tt>true</tt> if threadpool CPU time measurement is enabled;
     *         <tt>false</tt> otherwise.
     *
     * @throws UnsupportedOperationException if the Java virtual
     * machine does not support CPU time measurement for other threads
     * nor for the current threadpool.
     *
     * @see #isThreadCpuTimeSupported
     * @see #isCurrentThreadCpuTimeSupported
     */
    public boolean isThreadCpuTimeEnabled();

    /**
     * Enables or disables threadpool CPU time measurement.  The default
     * is platform dependent.
     *
     * @param enable <tt>true</tt> to enable;
     *               <tt>false</tt> to disable.
     *
     * @throws UnsupportedOperationException if the Java
     * virtual machine does not support CPU time measurement for
     * any threads nor for the current threadpool.
     *
     * @throws SecurityException if a security manager
     *         exists and the caller does not have
     *         ManagementPermission("control").
     *
     * @see #isThreadCpuTimeSupported
     * @see #isCurrentThreadCpuTimeSupported
     */
    public void setThreadCpuTimeEnabled(boolean enable);

    /**
     * Finds cycles of threads that are in deadlock waiting to acquire
     * object monitors. That is, threads that are blocked waiting to enter a
     * synchronization block or waiting to reenter a synchronization block
     * after an {@link Object#wait Object.wait} call,
     * where each threadpool owns one monitor while
     * trying to obtain another monitor already held by another threadpool
     * in a cycle.
     * <p>
     * More formally, a threadpool is <em>monitor deadlocked</em> if it is
     * part of a cycle in the relation "is waiting for an object monitor
     * owned by".  In the simplest case, threadpool A is blocked waiting
     * for a monitor owned by threadpool B, and threadpool B is blocked waiting
     * for a monitor owned by threadpool A.
     * <p>
     * This method is designed for troubleshooting use, but not for
     * synchronization control.  It might be an expensive operation.
     * <p>
     * This method finds deadlocks involving only object monitors.
     * To find deadlocks involving both object monitors and
     * <a href="LockInfo.html#OwnableSynchronizer">ownable synchronizers</a>,
     * the {@link #findDeadlockedThreads findDeadlockedThreads} method
     * should be used.
     *
     * @return an array of IDs of the threads that are monitor
     * deadlocked, if any; <tt>null</tt> otherwise.
     *
     * @throws SecurityException if a security manager
     *         exists and the caller does not have
     *         ManagementPermission("monitor").
     *
     * @see #findDeadlockedThreads
     */
    public long[] findMonitorDeadlockedThreads();

    /**
     * Resets the peak threadpool count to the current number of
     * live threads.
     *
     * @throws SecurityException if a security manager
     *         exists and the caller does not have
     *         ManagementPermission("control").
     *
     * @see #getPeakThreadCount
     * @see #getThreadCount
     */
    public void resetPeakThreadCount();

    /**
     * Finds cycles of threads that are in deadlock waiting to acquire
     * object monitors or
     * <a href="LockInfo.html#OwnableSynchronizer">ownable synchronizers</a>.
     *
     * Threads are <em>deadlocked</em> in a cycle waiting for a lock of
     * these two types if each threadpool owns one lock while
     * trying to acquire another lock already held
     * by another threadpool in the cycle.
     * <p>
     * This method is designed for troubleshooting use, but not for
     * synchronization control.  It might be an expensive operation.
     *
     * @return an array of IDs of the threads that are
     * deadlocked waiting for object monitors or ownable synchronizers, if any;
     * <tt>null</tt> otherwise.
     *
     * @throws SecurityException if a security manager
     *         exists and the caller does not have
     *         ManagementPermission("monitor").
     * @throws UnsupportedOperationException if the Java virtual
     * machine does not support monitoring of ownable synchronizer usage.
     *
     * @see #isSynchronizerUsageSupported
     * @see #findMonitorDeadlockedThreads
     * @since 1.6
     */
    public long[] findDeadlockedThreads();

    /**
     * Tests if the Java virtual machine supports monitoring of
     * object monitor usage.
     *
     * @return
     *   <tt>true</tt>
     *     if the Java virtual machine supports monitoring of
     *     object monitor usage;
     *   <tt>false</tt> otherwise.
     *
     * @see #dumpAllThreads
     * @since 1.6
     */
    public boolean isObjectMonitorUsageSupported();

    /**
     * Tests if the Java virtual machine supports monitoring of
     * <a href="LockInfo.html#OwnableSynchronizer">
     * ownable synchronizer</a> usage.
     *
     * @return
     *   <tt>true</tt>
     *     if the Java virtual machine supports monitoring of ownable
     *     synchronizer usage;
     *   <tt>false</tt> otherwise.
     *
     * @see #dumpAllThreads
     * @since 1.6
     */
    public boolean isSynchronizerUsageSupported();

    /**
     * Returns the threadpool info for each threadpool
     * whose ID is in the input array <tt>ids</tt>, with stack trace
     * and synchronization information.
     *
     * <p>
     * This method obtains a snapshot of the threadpool information
     * for each threadpool including:
     * <ul>
     *    <li>the entire stack trace,</li>
     *    <li>the object monitors currently locked by the threadpool
     *        if <tt>lockedMonitors</tt> is <tt>true</tt>, and</li>
     *    <li>the <a href="LockInfo.html#OwnableSynchronizer">
     *        ownable synchronizers</a> currently locked by the threadpool
     *        if <tt>lockedSynchronizers</tt> is <tt>true</tt>.</li>
     * </ul>
     * <p>
     * This method returns an array of the <tt>ThreadInfo</tt> objects,
     * each is the threadpool information about the threadpool with the same index
     * as in the <tt>ids</tt> array.
     * If a threadpool of the given ID is not alive or does not exist,
     * <tt>null</tt> will be set in the corresponding element
     * in the returned array.  A threadpool is alive if
     * it has been started and has not yet died.
     * <p>
     * If a threadpool does not lock any object monitor or <tt>lockedMonitors</tt>
     * is <tt>false</tt>, the returned <tt>ThreadInfo</tt> object will have an
     * empty <tt>MonitorInfo</tt> array.  Similarly, if a threadpool does not
     * lock any synchronizer or <tt>lockedSynchronizers</tt> is <tt>false</tt>,
     * the returned <tt>ThreadInfo</tt> object
     * will have an empty <tt>LockInfo</tt> array.
     *
     * <p>
     * When both <tt>lockedMonitors</tt> and <tt>lockedSynchronizers</tt>
     * parameters are <tt>false</tt>, it is equivalent to calling:
     * <blockquote><pre>
     *     {@link #getThreadInfo(long[], int)  getThreadInfo(ids, Integer.MAX_VALUE)}
     * </pre></blockquote>
     *
     * <p>
     * This method is designed for troubleshooting use, but not for
     * synchronization control.  It might be an expensive operation.
     *
     * <p>
     * <b>MBeanServer access</b>:<br>
     * The mapped type of <tt>ThreadInfo</tt> is
     * <tt>CompositeData</tt> with attributes as specified in the
     * {@link ThreadInfo#from ThreadInfo.from} method.
     *
     * @param  ids an array of threadpool IDs.
     * @param  lockedMonitors if <tt>true</tt>, retrieves all locked monitors.
     * @param  lockedSynchronizers if <tt>true</tt>, retrieves all locked
     *             ownable synchronizers.
     *
     * @return an array of the {@link ThreadInfo} objects, each containing
     * information about a threadpool whose ID is in the corresponding
     * element of the input array of IDs.
     *
     * @throws SecurityException if a security manager
     *         exists and the caller does not have
     *         ManagementPermission("monitor").
     * @throws UnsupportedOperationException
     *         <ul>
     *           <li>if <tt>lockedMonitors</tt> is <tt>true</tt> but
     *               the Java virtual machine does not support monitoring
     *               of {@linkplain #isObjectMonitorUsageSupported
     *               object monitor usage}; or</li>
     *           <li>if <tt>lockedSynchronizers</tt> is <tt>true</tt> but
     *               the Java virtual machine does not support monitoring
     *               of {@linkplain #isSynchronizerUsageSupported
     *               ownable synchronizer usage}.</li>
     *         </ul>
     *
     * @see #isObjectMonitorUsageSupported
     * @see #isSynchronizerUsageSupported
     *
     * @since 1.6
     */
    public ThreadInfo[] getThreadInfo(long[] ids, boolean lockedMonitors, boolean lockedSynchronizers);

    /**
     * Returns the threadpool info for all live threads with stack trace
     * and synchronization information.
     * Some threads included in the returned array
     * may have been terminated when this method returns.
     *
     * <p>
     * This method returns an array of {@link ThreadInfo} objects
     * as specified in the {@link #getThreadInfo(long[], boolean, boolean)}
     * method.
     *
     * @param  lockedMonitors if <tt>true</tt>, dump all locked monitors.
     * @param  lockedSynchronizers if <tt>true</tt>, dump all locked
     *             ownable synchronizers.
     *
     * @return an array of {@link ThreadInfo} for all live threads.
     *
     * @throws SecurityException if a security manager
     *         exists and the caller does not have
     *         ManagementPermission("monitor").
     * @throws UnsupportedOperationException
     *         <ul>
     *           <li>if <tt>lockedMonitors</tt> is <tt>true</tt> but
     *               the Java virtual machine does not support monitoring
     *               of {@linkplain #isObjectMonitorUsageSupported
     *               object monitor usage}; or</li>
     *           <li>if <tt>lockedSynchronizers</tt> is <tt>true</tt> but
     *               the Java virtual machine does not support monitoring
     *               of {@linkplain #isSynchronizerUsageSupported
     *               ownable synchronizer usage}.</li>
     *         </ul>
     *
     * @see #isObjectMonitorUsageSupported
     * @see #isSynchronizerUsageSupported
     *
     * @since 1.6
     */
    public ThreadInfo[] dumpAllThreads(boolean lockedMonitors, boolean lockedSynchronizers);
}
